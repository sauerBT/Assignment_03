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
            // 1. Transmit a getScore to the model and a render request to the View
            Transmissions.transmitScore(view, model, this.outStream);
            String currentCommandLine = scan.nextLine(); // MUTATION: Extract next command line
            String command = CommandParser.parseCommand(currentCommandLine).toLowerCase(); // TODO -- this could be its own data type (enum)
            List<Integer> inputs = CommandParser.parseInputs(currentCommandLine);
            String executedCommand = this.executeCommand(model, view, inputs, command);
            if (!executedCommand.equals("q")) {
                scanForInputRequestsHelper(model, view, scan);
            }
        } else if (!model.isGameOver()) {
            Transmissions.transmitScore(view, model, this.outStream);
            scanForInputRequestsHelper(model, view, new Scanner(this.inStream));
        }
    }

    private String executeCommand(PyramidSolitaireModel<?> model, PyramidSolitaireView view, List<Integer> inputs, String command) {
        return switch (command) {
            case "rm1" -> CommandHandler.runRemoveSingle(model, inputs);
            case "rm2" -> CommandHandler.runRemoveDouble(model, inputs);
            case "rmwd" -> CommandHandler.runRemoveUsingDraw(model, inputs);
            case "dd" -> CommandHandler.runDiscardDraw(model, inputs);
            case "q" -> CommandHandler.quit(model, view, this.outStream);
            default -> "None";
            };
    }

}

class CommandParser {
    /**
     * Produce the user command for the given command line.
     *
     * @param commandLine The given command line
     * @return The command name.
     */
    public static String parseCommand(String commandLine) {
        Scanner commandLineScan = new Scanner(commandLine);
        if (commandLineScan.hasNext()) {
            return commandLineScan.next();
        } else {
            return "None";
        }
    }

    /**
     * Produce the command inputs for the given command line.
     *
     * @param commandLine The given command line.
     * @return The command inputs as an integer array.
     */
    public static List<Integer> parseInputs(String commandLine) {
        Scanner commandLineScan = new Scanner(commandLine);
        commandLineScan.next();
        return parseInputsHelper(commandLineScan, new ArrayList<>());
    }
    private static List<Integer> parseInputsHelper(Scanner commandLineScan, List<Integer> inputAcc) {
        if (!commandLineScan.hasNextInt()) {
            return inputAcc;
        } else {
            inputAcc.add(commandLineScan.nextInt());
            return parseInputsHelper(commandLineScan, Util.ListUtil.clone(inputAcc));
        }
    }
}

class Transmissions {

    public static void transmitQuit(PyramidSolitaireModel<?> model, PyramidSolitaireView view, Appendable outStream) {
        try {
            view.renderQuit();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to render quitting game state" + e);
        }
    }

    public static void transmitScore(PyramidSolitaireView view, PyramidSolitaireModel<?> model, Appendable outStream) {
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

}

class CommandHandler {

    public static String runRemoveSingle(PyramidSolitaireModel<?> model, List<Integer> commandInputs) {
        int row = commandInputs.get(0);
        int card = commandInputs.get(1);
        model.remove(row, card);
        return "rm1";
    }

    public static String runRemoveDouble(PyramidSolitaireModel<?> model, List<Integer> commandInputs) {
        int row1  = commandInputs.get(0);
        int card1 = commandInputs.get(1);
        int row2  = commandInputs.get(2);
        int card2 = commandInputs.get(3);
        model.remove(row1, card1, row2, card2);
        return "rm2";
    }

    public static String runRemoveUsingDraw(PyramidSolitaireModel<?> model, List<Integer> commandInputs) {
        int drawIndex = commandInputs.get(0);
        int row       = commandInputs.get(1);
        int card      = commandInputs.get(2);
        model.removeUsingDraw(drawIndex, row, card);
        return "rmwd";
    }

    public static String runDiscardDraw(PyramidSolitaireModel<?> model, List<Integer> commandInputs) {
        int drawIndex = commandInputs.getFirst();
        model.discardDraw(drawIndex);
        return "dd";
    }

    public static String quit(PyramidSolitaireModel<?> model, PyramidSolitaireView view, Appendable outStream) {
        Transmissions.transmitQuit(model, view, outStream);
        return "q";
    }
}