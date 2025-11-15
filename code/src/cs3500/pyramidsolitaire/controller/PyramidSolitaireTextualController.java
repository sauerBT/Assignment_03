package cs3500.pyramidsolitaire.controller;

import cs3500.pyramidsolitaire.model.hw02.PyramidSolitaireModel;
import cs3500.pyramidsolitaire.model.hw02.Util;

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
            transmitStartGame(model, deck, shuffle, numRows, numDraw);
            scanForInputRequests(model);
        } else {
            throw new IllegalArgumentException("Provided model cannot be null!");
        }
    };

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
    private <K> void transmitStartGame(PyramidSolitaireModel<K> model, List<K> deck, boolean shuffle, int numRows, int numDraw) {
        model.startGame(deck, shuffle, numRows, numDraw);
    }

    /**
     * Given a game model, scan for user requests and change the game state based on the given commands.
     *
     * @param model The game model.
     */
    private void scanForInputRequests(PyramidSolitaireModel<?> model) {
        this.scanForInputRequestsHelper(model, new Scanner(this.inStream));
    }

    private void scanForInputRequestsHelper(PyramidSolitaireModel<?> model, Scanner scan) {
        if (scan.hasNextLine()) {
            String currentCommandLine = scan.nextLine(); // MUTATION: Extract next command line
            String command = parseCommand(currentCommandLine); // TODO -- this could be its own data type (enum)
            List<Integer> inputs = parseInputs(currentCommandLine);
            String executedCommand = this.executeCommand(model, inputs, command);
            if (!executedCommand.equals("q")) {
                scanForInputRequestsHelper(model, scan);
            }
        } else {
            scanForInputRequestsHelper(model, new Scanner(this.inStream));
        }
    }

    /**
     * Produce the user command for the given command line.
     *
     * @param commandLine The given command line
     * @return The command name.
     */
    private String parseCommand(String commandLine) {
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
    private List<Integer> parseInputs(String commandLine) {
        Scanner commandLineScan = new Scanner(commandLine);
        commandLineScan.next();
        return parseInputsHelper(commandLineScan, new ArrayList<>());
    }
    private List<Integer> parseInputsHelper(Scanner commandLineScan, List<Integer> inputAcc) {
        if (!commandLineScan.hasNextInt()) {
            return inputAcc;
        } else {
            inputAcc.add(commandLineScan.nextInt());
            return parseInputsHelper(commandLineScan, Util.ListUtil.clone(inputAcc));
        }
    }

    private String executeCommand(PyramidSolitaireModel<?> model, List<Integer> inputs, String command) {
        return switch (command) {
            case "rm1" -> this.runRemoveSingle(model, inputs);
            case "rm2" -> this.runRemoveDouble(model, inputs);
            case "rmwd" -> this.runRemoveUsingDraw(model, inputs);
            case "dd" -> this.runDiscardDraw(model, inputs);
            case "q" -> this.quit(model);
            default -> "None";
            };
    }

    private String runRemoveSingle(PyramidSolitaireModel<?> model, List<Integer> commandInputs) {
        int row = commandInputs.get(0);
        int card = commandInputs.get(1);
        model.remove(row, card);
        return "rm1";
    }

    private String runRemoveDouble(PyramidSolitaireModel<?> model, List<Integer> commandInputs) {
        int row1  = commandInputs.get(0);
        int card1 = commandInputs.get(1);
        int row2  = commandInputs.get(2);
        int card2 = commandInputs.get(3);
        model.remove(row1, card1, row2, card2);
        return "rm2";
    }

    private String runRemoveUsingDraw(PyramidSolitaireModel<?> model, List<Integer> commandInputs) {
        int drawIndex = commandInputs.get(0);
        int row       = commandInputs.get(1);
        int card      = commandInputs.get(2);
        model.removeUsingDraw(drawIndex, row, card);
        return "rmwd";
    }

    private String runDiscardDraw(PyramidSolitaireModel<?> model, List<Integer> commandInputs) {
        int drawIndex = commandInputs.getFirst();
        model.discardDraw(drawIndex);
        return "dd";
    }

    private String quit(PyramidSolitaireModel<?> model) {
        return "q";
    }
}


