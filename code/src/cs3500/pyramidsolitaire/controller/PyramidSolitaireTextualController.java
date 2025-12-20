package cs3500.pyramidsolitaire.controller;

import cs3500.pyramidsolitaire.model.hw02.PyramidSolitaireModel;
import cs3500.pyramidsolitaire.model.hw02.Util;
import cs3500.pyramidsolitaire.view.PyramidSolitaireTextualView;
import cs3500.pyramidsolitaire.view.PyramidSolitaireView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PyramidSolitaireTextualController implements PyramidSolitaireController {
    Readable inStream;
    Appendable outStream;

    public PyramidSolitaireTextualController(Readable inStream, Appendable outStream) {
        if ((inStream != null) && (outStream != null)) {
            this.inStream = inStream;
            this.outStream = outStream;
        } else {
            if (inStream == null) {
                throw new IllegalArgumentException("Must provide an instream!");
            } else {
                throw new IllegalArgumentException("Must provide an outstream!");
            }
        }
    }

    public Readable inStream() { return this.inStream; }
    public Appendable outStream() { return this.outStream; }

    // TODO
    @Override
    public <K> void playGame(PyramidSolitaireModel<K> model, List<K> deck, boolean shuffle, int numRows, int numDraw) {
        if (model != null) {
            PyramidSolitaireView view = createView(model);
            Transmissions.transmitStartGame(model, deck, shuffle, numRows, numDraw);
            scanForInputRequests(model, view);
        } else {
            throw new IllegalArgumentException("Provided model cannot be null!");
        }
    };

    private <K> PyramidSolitaireView createView(PyramidSolitaireModel<K> model) {
        return new PyramidSolitaireTextualView(model, this.outStream);
    }

    /**
     * Given a game model, scan for user requests and change the game state based on the given commands.
     *
     * @param model The game model.
     */
    private void scanForInputRequests(PyramidSolitaireModel<?> model, PyramidSolitaireView view) {
        this.scanForInputRequestsHelper(model, view, new Scanner(this.inStream));
    }

    private void scanForInputRequestsHelper(PyramidSolitaireModel<?> model, PyramidSolitaireView view, Scanner scan) {
        // 1. Transmit a render request to the View
        Transmissions.transmitRender(view);
        if (scan.hasNextLine() && !model.isGameOver()) {
            // 2. Transmit a getScore to the model and a render request to the View
            Transmissions.transmitScore(view);
            String currentCommandLine = scan.nextLine(); // MUTATION: Extract next command line
            RequestParser request =
                    RequestParser.parseCommand(currentCommandLine); // 3. Extract command (or throw error)
            String executedCommand = CommandHandler.executeCommand(model, view, request, this);
            if (!executedCommand.equals("q")) {
                scanForInputRequestsHelper(model, view, scan);
            }
        } else if (!model.isGameOver()) {
            Transmissions.transmitScore(view);
            scanForInputRequestsHelper(model, view, new Scanner(this.inStream));
        }
    }

    

}

class RequestParser {
    private final String command;
    private final List<Integer> inputs;
    private final Scanner commandLine;

    private RequestParser(String command, Scanner commandLine) {
        this.command = command;
        this.commandLine = commandLine;
        this.inputs = new ArrayList<>();
    }

    private RequestParser(String command, List<Integer> inputs, Scanner commandLine) {
        if (CommandHandler.isValidCommand(command)) {
            this.command = command;
            this.inputs = inputs;
            this.commandLine = commandLine;
        } else {
            throw new IllegalArgumentException(String.format("Input does not contain a valid command. Inputs: %s", command));
        }
    }

    String command() { return this.command; }

    List<Integer> inputs() { return this.inputs; }

    Scanner commandLine() { return this.commandLine; }

    /**
     * Produce the user command for the given command line.
     *
     * @param commandLine The given command line
     * @return The command name.
     */
    public static RequestParser parseCommand(String commandLine) {
        Scanner commandLineScan = new Scanner(commandLine);
        if (commandLineScan.hasNext()) {
            return new RequestParser(commandLineScan.next(), commandLineScan);
        } else {
            throw new IllegalStateException(String.format("Invalid input. Input: %s", commandLineScan));
        }
    }

    /**
     * Produce the command inputs for the given command line.
     *
     * @param commandLine The given command line.
     * @return The command inputs as an integer array.
     */
    public RequestParser parseInputs(Scanner commandLine, Integer numberOfArgs, PyramidSolitaireView view, PyramidSolitaireController controller) {
        return parseInputsHelper(commandLine, new ArrayList<>(), 0, numberOfArgs, view, controller);
    }

    // TODO
    private RequestParser parseInputsHelper(Scanner commandLineScan, List<Integer> inputAcc, Integer inputCounter, Integer numberOfArgs, PyramidSolitaireView view, PyramidSolitaireTextualController controller) {
        if (!commandLineScan.hasNextInt()) {
            if (inputCounter < numberOfArgs) {
                Transmissions.askForAdditionalInputs(view);
                return parseInputsHelper(controller.inStream(), Util.ListUtil.clone(inputAcc), inputCounter, numberOfArgs, view, controller);
            } else {
                return new RequestParser(this.command, inputAcc, commandLineScan);
            }
        } else {
            inputAcc.add(commandLineScan.nextInt());
            return parseInputsHelper(commandLineScan, Util.ListUtil.clone(inputAcc), inputCounter + 1, numberOfArgs, view, controller);
        }
    }
}

class Transmissions {

    public static void transmitQuit(PyramidSolitaireView view) {
        try {
            view.renderQuit();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to render quitting game state" + e);
        }
    }

    public static void transmitScore(PyramidSolitaireView view) {
        try {
            view.renderScore();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to render score" + e);
        }
    }

    /**
     * Transmit render request to the view.
     *
     * @param view The given view for the application.
     * @throws IllegalStateException When view cannot complete rendering due to an IOException.
     */
    public static void transmitRender(PyramidSolitaireView view) {
        try {
            view.render();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to render view" + e);
        }
    }

    // TODO -- should this be moved to a CommandHandler?
    /**
     * Send request to given model to start the game with the given conditions.
     * MUTATION: Sends a message to the game model to run the startGame method.
     *
     * @param deck Deck of elements to be used for the game.
     * @param shuffle Determines if the deck should be shuffled prior to start of the game.
     * @param numRows Initial number of rows in the game.
     * @param numDraw Number of draw cards from the stock during the game.
     * @param <K> Type of elements that make up the game pyramid.
     */
    public static <K> void transmitStartGame(PyramidSolitaireModel<K> model, List<K> deck, boolean shuffle, int numRows, int numDraw) {
        model.startGame(deck, shuffle, numRows, numDraw);
    }

    // TODO
    public static void askForAdditionalInputs(PyramidSolitaireView view) {}
}

class CommandHandler {

    static String executeCommand(PyramidSolitaireModel<?> model, PyramidSolitaireView view, RequestParser request) {
        return switch (request.command().toLowerCase()) {
            case "rm1" -> CommandHandler.runRemoveSingle(model, view, request);
            case "rm2" -> CommandHandler.runRemoveDouble(model, view, request);
            case "rmwd" -> CommandHandler.runRemoveUsingDraw(model, view, request);
            case "dd" -> CommandHandler.runDiscardDraw(model, view, request);
            case "q" -> CommandHandler.quit(view);
            default -> "None";
        };
    }

    static boolean isValidCommand(String command) {
        return switch (command) {
            case "rm1" -> true;
            case "rm2" -> true;
            case "rmwd" -> true;
            case "dd" -> true;
            case "q" -> true;
            default -> false;
        };
    }

    public static String runRemoveSingle(PyramidSolitaireModel<?> model, PyramidSolitaireView view, RequestParser request) {
        List<Integer> commandInputs = request.parseInputs(request.commandLine(), 2, view).inputs();
        int row = commandInputs.get(0);
        int card = commandInputs.get(1);
        model.remove(row, card);
        return "rm1";
    }

    public static String runRemoveDouble(PyramidSolitaireModel<?> model, PyramidSolitaireView view, RequestParser request) {
        List<Integer> commandInputs = request.parseInputs(request.commandLine(), 4, view).inputs();
        int row1  = commandInputs.get(0);
        int card1 = commandInputs.get(1);
        int row2  = commandInputs.get(2);
        int card2 = commandInputs.get(3);
        model.remove(row1, card1, row2, card2);
        return "rm2";
    }

    public static String runRemoveUsingDraw(PyramidSolitaireModel<?> model, PyramidSolitaireView view, RequestParser request) {
        List<Integer> commandInputs = request.parseInputs(request.commandLine(), 3, view).inputs();
        int drawIndex = commandInputs.get(0);
        int row       = commandInputs.get(1);
        int card      = commandInputs.get(2);
        model.removeUsingDraw(drawIndex, row, card);
        return "rmwd";
    }

    public static String runDiscardDraw(PyramidSolitaireModel<?> model, PyramidSolitaireView view, RequestParser request) {
        List<Integer> commandInputs = request.parseInputs(request.commandLine(), 1, view).inputs();
        int drawIndex = commandInputs.getFirst();
        model.discardDraw(drawIndex);
        return "dd";
    }

    public static String quit(PyramidSolitaireView view) {
        Transmissions.transmitQuit(view);
        return "q";
    }
}