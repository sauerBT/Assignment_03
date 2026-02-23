package cs3500.pyramidsolitaire.controller;

import cs3500.pyramidsolitaire.model.hw02.PyramidSolitaireModel;
import cs3500.pyramidsolitaire.model.hw02.Util;
import cs3500.pyramidsolitaire.view.PyramidSolitaireTextualView;
import cs3500.pyramidsolitaire.view.PyramidSolitaireView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * The controller for playing a game of Pyramid Solitaire.
 *<p>
 *     Controller used for IO interactions to drive model updates and interface with the view.
 *</p>
 *
 * @author Brian Sauerborn
 * @version 1.0
 * @since 1.0
 * @see PyramidSolitaireController
 */
public class PyramidSolitaireTextualController implements PyramidSolitaireController {
    /**
     * The output stream issued to a user.
     *
     * @since 1.0
     */
    Appendable outStream;
    StreamHandler stream;

    public PyramidSolitaireTextualController(Readable inStream, Appendable outStream) {
        if ((inStream != null) && (outStream != null)) {
            this.stream = StreamHandler.of(inStream);
            this.outStream = outStream;
        } else {
            if (inStream == null) {
                throw new IllegalArgumentException("Must provide an instream!");
            } else {
                throw new IllegalArgumentException("Must provide an outstream!");
            }
        }
    }

    Appendable outStream() { return this.outStream; }

    @Override
    public <K> void playGame(PyramidSolitaireModel<K> model, List<K> deck, boolean shuffle, int numRows, int numDraw) {
        if (model != null) {
            PyramidSolitaireView view = createView(model);
            Transmissions.transmitStartGame(model, deck, shuffle, numRows, numDraw);
            this.run(model, view);
        } else {
            throw new IllegalArgumentException("Provided model cannot be null!");
        }
    };

    /**
     * Produce the given View.
     * @param model TODO
     * @return TODO
     * @param <K> TODO
     */
    private <K> PyramidSolitaireView createView(PyramidSolitaireModel<K> model) {
        return new PyramidSolitaireTextualView(model, this.outStream);
    }

    /** TODO
     * Run the game, monitoring for given inputs from a player and executing requested commands.
     * @param model TODO
     * @param view TODO
     */
    private <K> void run(PyramidSolitaireModel<K> model, PyramidSolitaireView view) {
        if (model.isGameOver()) {
            Transmissions.transmitRender(view);
            // GAME ENDS!!!
            return;
        }
        Transmissions.transmitRender(view);
        Transmissions.transmitScore(view);
        // 2. Generate a CommandExecutor from the given request, validate the command name
        CommandExecutor executor =
                execute(inputExtractionLoop(commandExtractionLoop(model), model, view));
        System.out.println("Controller| executor = " + executor.toString());

        // 3. Check if returned command is a quit command, end game if it is
        if (executor.isQuitCommand()) {
            Transmissions.transmitQuit(view);
            return;
        }

        this.run(model, view);
    }

    /**
     * Produce a Request from this stream.
     * @return The extracted Request.
     */
    private Request requestExtractionLoop() {
        return this.stream.extractRequest()
                 .orElseGet(this::requestExtractionLoop);
    }

    /**
     * Produce a "valid" CommandExecutor.
     * <p>
     *     Valid, relative to this method, indicates that the CommandExecutor is considered a recognized command capable
     *     of being executed on. However, valid does not mean that the current number of inputs specified for the
     *     valid command has met all appropriate criteria. Input validation must be handled separately.
     *     NOTE: This extraction method is coupled with the requestExtractionLoop() method
     * </p>
     * @param model A given Model for the CommandExecutor to interact with.
     * @return The CommandExecutor
     */
//    private CommandExecutor handleInvalidCommand(Request request, PyramidSolitaireModel<?> model) { return CommandExecutor.of(model, Command.of(CommandName.q, new ArrayList<>())); } // STUB
    private CommandExtractionPackage<CommandExecutor> commandExtractionLoop(PyramidSolitaireModel<?> model) {
        Request request = requestExtractionLoop();
        return CommandExecutor.of(model, request)
                .orElseGet(() -> { return this.commandExtractionLoop(model); });
    }

    /**
     * Produce a "Complete" CommandExecutor.
     * <p>
     *     Complete, relative to this method, indicates that the CommandExecutor is considered an executable command,
     *     meaning it has a valid command name and number of inputs.
     * </p>
     * @param extractionPackage A CommandExecutor with a Valid command and command name. TODO
     * @param model A given Model for the CommandExecutor to interact with.
     * @return The "Complete" CommandExecutor
     */
    private CommandExecutor inputExtractionLoop(CommandExtractionPackage<CommandExecutor> extractionPackage, PyramidSolitaireModel<?> model, PyramidSolitaireView view) {
        return inputExtractionLoopHelper(extractionPackage.data(), extractionPackage.request(), model, view);
    }
    private CommandExecutor inputExtractionLoopHelper(CommandExecutor executor, Request request, PyramidSolitaireModel<?> model, PyramidSolitaireView view) {

        Optional<CommandExecutor> quitCommand = handleQuitCommandDuringInput(request, model);
        if (quitCommand.isPresent()) {
            return quitCommand.get();
        } else {
            CommandExecutor updatedExecutor =
                    CommandExecutor.of(
                            model,
                            Command.of(
                                    executor.command.name(),
                                    CommandExecutor.extractInputsFromRequest(request, executor.command.inputs())
                            )
                    );
            System.out.println("Controller| Current request (for input): " + request.commandLine);
            if (updatedExecutor.isComplete()) {
                return updatedExecutor;
            } else {
                Transmissions.askForAdditionalInputs(view);
                return inputExtractionLoopHelper(updatedExecutor, requestExtractionLoop(), model, view);
            }
        }
    }

    private Optional<CommandExecutor> handleQuitCommandDuringInput(Request request, PyramidSolitaireModel<?> model) {
        Optional<CommandExtractionPackage<CommandExecutor>> executorCheck = CommandExecutor.of(model, request);
        if (executorCheck.isEmpty()) {
            return Optional.empty();
        } else if (executorCheck.get().data().isQuitCommand()) {
            return Optional.of(executorCheck.get().data());
        } else {
            return Optional.empty();
        }
    }
    
    /** TODO
     * Given a "Valid" and "Complete" CommandExecutor, execute the command against the model and produce a successfully
     * executed command
     * @param executor The given "Valid" and "Complete" CommandExecutor.
     * @return The successfully executed CommandExecutor.
     */
    private CommandExecutor execute(CommandExecutor executor) {
        return executor.executeCommand();
    }

}

/**
 * The request handler for working with an input stream of commands from a User's external input.
 *<p>
 *     The request handler monitors an in stream for request from a User. It is capable of extracting a line of user
 *     input from an input stream and extracting a Request from that stream.
 *</p>
 *
 * @author Brian Sauerborn
 * @version 1.0
 * @since 1.0
 */
class StreamHandler {
    /**
     * The input stream coming from a user.
     *
     * @since 1.0
     */
    private final Scanner inStreamScanner;

    private StreamHandler(Readable inStream) {
        this.inStreamScanner = new Scanner(inStream);
    }

    /**
     * Produce a StreamHandler from a single line of a given command line readable.
     * @param inStream A string containing a request candidate.
     * @return A StreamHandler
     */
    public static StreamHandler of(Readable inStream) { return new StreamHandler(inStream); }

    /**
     * Produce a new Request from this input stream, unless there is no existing request.
     * MUTATION: This method mutates the inStream field of this object.
     * @return Optional representing an extracted Request if TRUE, otherwise empty.
     */
    public Optional<Request> extractRequest() {
//        Scanner instreamScanner = new Scanner(this.inStream);
        if (this.inStreamScanner.hasNextLine()) {
            String requestValue = this.inStreamScanner.nextLine();
            System.out.println("StreamHandler| New Request from instream: " + requestValue);
            return Optional.of(Request.of(requestValue));
        } else {
            System.out.println("StreamHandler| No new request extracted from instream");
            return Optional.empty();
        }
    }
}

/**
 * The request for maintaining the state and possible interactions with a User's external input.
 *<p>
 *     The Request handles a single request from a User, in this case defined as a single line from an input IO stream.
 *     The Request know everything about how to extract information from a single command line of input provided by a
 *     user.
 *</p>
 *
 * @author Brian Sauerborn
 * @version 1.0
 * @since 1.0
 */
class Request {
    /**
     * A single line from an IO stream coming from an external source.
     *
     * @since 1.0
     */
    String commandLine;

    private Request(String commandLine) {
        this.commandLine = commandLine;
    }

    /**
     * Produce a request containing a single line from an input IO stream.
     * @param commandLine A single line (as a String) from a Readable Stream.
     * @return A Request
     */
    public static Request of(String commandLine) { return new Request(commandLine); }

    /** TODO
     * Produce the next input from this commandLine as an Integer, or produce nothing if there is no next input
     * @return Integer command input.
     */
//    protected Optional<Integer> extractInput() { return Optional.empty(); } // STUB
    protected Optional<CommandExtractionPackage<Integer>> extractInput() {
        Scanner commandLineScanner = new Scanner(this.commandLine);
        if (commandLineScanner.hasNextInt()) {
            Integer extractedInteger = commandLineScanner.nextInt();
            commandLineScanner.useDelimiter("\\A");
            String updatedCommand = commandLineScanner.hasNext() ? commandLineScanner.next() : "";
            commandLineScanner.close();
            return Optional.of(CommandExtractionPackage.of(extractedInteger, Request.of(updatedCommand)));
        } else {
            return Optional.empty();
        }
    }

    /** TODO
     * Produce the next command from this commandLine as an String
     * @return String command.
     */
//    protected Optional<String> extractCommand() { return Optional.empty(); } // STUB
    protected Optional<CommandExtractionPackage<String>> extractCommand() {
        Scanner commandLineScanner = new Scanner(this.commandLine);
        if (commandLineScanner.hasNext()) {
            String extractedCommand = commandLineScanner.next();
            commandLineScanner.useDelimiter("\\A");
            String updatedCommand = commandLineScanner.hasNext() ? commandLineScanner.next() : "";
            commandLineScanner.close();
            return Optional.of(CommandExtractionPackage.of(extractedCommand, Request.of(updatedCommand)));
        } else {
            return Optional.empty();
        }
    }
}

/**
 * A CommandExecutor holds a Command and a Model and understands how a specific, unique Command may be parsed (the
 * valid command names and number of and types of inputs) and executed against a particular model.
 *
 * @author Brian Sauerborn
 * @version 1.0
 * @since 1.0
 */
class CommandExecutor {
    /**
     * The model a command is executed against.
     *
     * @since 1.0
     */
    final PyramidSolitaireModel<?> model;
    /**
     * The data holder for a command.
     *
     * @since 1.0
     */
    final Command command;

    private CommandExecutor(PyramidSolitaireModel<?> model, Command command) {
        this.model = model;
        this.command = command;
    }

    @Override
    public String toString() { return command.toString(); }

    /**
     * Produce an Optional containing a CommandExecutor given a Request.
     * @param model The application model the executor will execute a command against.
     * @param request The given Request parsed from a user input stream.
     * @return The Optional.
     */
    public static Optional<CommandExtractionPackage<CommandExecutor>> of(PyramidSolitaireModel<?> model, Request request) {
        Optional<CommandExtractionPackage<CommandName>> commandName = extractCommandFromRequest(request);
        return commandName.map(name -> CommandExtractionPackage.of(new CommandExecutor(model, Command.of(name.data(), new ArrayList<>())), name.request()));
    }

    public static CommandExecutor of(PyramidSolitaireModel<?> model, Command command) {
        return new CommandExecutor(model, command);
    }

    /**
     * Given a Request containing a candidate for a Command, produce the CommandName or else produce empty.
     *
     * @param request The given Request.
     * @return The CommandName
     */
    public static Optional<CommandExtractionPackage<CommandName>> extractCommandFromRequest(Request request) {
        Optional<CommandExtractionPackage<String>> commandOption = request.extractCommand();
        if (commandOption.isEmpty()) {
            System.out.println("CommandExecutor| Found no new Command string on given request!: " + request.commandLine);
            return Optional.empty();
        } else {
            String command = commandOption.get().data().toLowerCase();
            if (CommandName.isValid(command)) {
                System.out.println("CommandExecutor| New command found (by command executor): " + command);
                return Optional.of(CommandExtractionPackage.of(CommandName.valueOf(command), commandOption.get().request()));
            } else {
                System.out.println("CommandExecutor| Command not valid! String on given request!: " + command);
                return Optional.empty();
            }
        }
    }

    /** TODO
     * Given a Request containing a candidate for Inputs of a particular command, produce the Inputs for that command.
     *
     * @param request The given Request.
     * @return The inputs.
     */
    static List<Integer> extractInputsFromRequest(Request request, List<Integer> initialInputs) {
        return expectedNumberOfInputsHelper(request, initialInputs);
    }
    private static List<Integer> expectedNumberOfInputsHelper(Request request, List<Integer> inputAcc) {
        Optional<CommandExtractionPackage<Integer>> extractedInput = request.extractInput();
        if (extractedInput.isEmpty()) {
            System.out.println("CommandExecutor| Found no new Inputs!");
            return inputAcc;
        } else {
            System.out.println("CommandExecutor| Found new Inputs! Given request: " + extractedInput.get().data());
            inputAcc.add(extractedInput.get().data());
            return expectedNumberOfInputsHelper(extractedInput.get().request(), Util.ListUtil.clone(inputAcc));
        }
    }

    CommandExecutor executeCommand() {
        return switch (this.command.name()) {
            case CommandName.rm1 -> this.runRemoveSingle();
            case CommandName.rm2 -> this.runRemoveDouble();
            case CommandName.rmwd -> this.runRemoveUsingDraw();
            case CommandName.dd -> this.runDiscardDraw();
            case CommandName.q -> this.quit();
        };
    }

    boolean isComplete() { return this.command.inputs().size() >= this.expectedNumberOfInputs(); }

    Integer expectedNumberOfInputs() {
        return switch (this.command.name()) {
            case CommandName.rm1 -> 2;
            case CommandName.rm2 -> 4;
            case CommandName.rmwd -> 3;
            case CommandName.dd -> 1;
            case CommandName.q -> 0;
        };
    }

    boolean isQuitCommand() { return (this.command.name() == CommandName.q); }

    CommandExecutor runRemoveSingle() throws IllegalArgumentException {
        if (!this.isComplete()) {
            throw new IllegalArgumentException("Invalid number of inputs for the removeSingle method!");
        } else {
            try {
                int row = this.command.inputs().get(0);
                int card = this.command.inputs().get(1);
                this.model.remove(row, card);
                return this;
            } catch (IllegalArgumentException e) {
                System.out.println("Error running remove single command");
                return this;
            }
        }
    }

    CommandExecutor runRemoveDouble() throws IllegalArgumentException {
        if (!this.isComplete()) {
            throw new IllegalArgumentException("Invalid number of inputs for the removeSingle method!");
        } else {
            int row1 = this.command.inputs().get(0);
            int card1 = this.command.inputs().get(1);
            int row2 = this.command.inputs().get(2);
            int card2 = this.command.inputs().get(3);
            this.model.remove(row1, card1, row2, card2);
            return this;
        }
    }

    CommandExecutor runRemoveUsingDraw() throws IllegalArgumentException {
            if (!this.isComplete()) {
                throw new IllegalArgumentException("Invalid number of inputs for the removeSingle method!");
            } else {
                try {
                    int drawIndex = this.command.inputs().get(0);
                    int row = this.command.inputs().get(1);
                    int card = this.command.inputs().get(2);
                    this.model.removeUsingDraw(drawIndex, row, card);
                    return this;
                } catch (IllegalArgumentException e) {
                System.out.println("Invalid move. Play again. " + e);
                return this;
                }
            }
    }

    CommandExecutor runDiscardDraw() throws IllegalArgumentException {
            if (!this.isComplete()) {
                throw new IllegalArgumentException("Invalid number of inputs for the removeSingle method!");
            } else {
                int drawIndex = this.command.inputs().getFirst();
                this.model.discardDraw(drawIndex);
                return this;
            }
    }

    CommandExecutor quit() throws IllegalArgumentException {
            if (!this.isComplete()) {
                throw new IllegalArgumentException("Invalid number of inputs for the removeSingle method!");
            } else {
                return this;
            }
    }
}

/**
 * A generic command used as a container for a single command that will interact with a Model.
 *<p>
 *     The Command represents a single executable command exposed by a Model's public facing interface. This class does
 *     NOT, however, know HOW to interact with the Model OR understand the differences between the individual commands
 *     it represents. Therefore, this class has a STRONG association to a CommandExecutor class, which takes a functional
 *     approach to understanding the housed command (i.e. what makes a command valid and complate) and HOW it interacts
 *     with a Model.
 *</p>
 *
 * @author Brian Sauerborn
 * @version 1.0
 * @since 1.0
 */
class Command {
    /**
     * The name of the command.
     *
     * @since 1.0
     */
    private final CommandName name;
    /**
     * The inputs the command requires for execution.
     *
     * @since 1.0
     */
    private final List<Integer> inputs;

    private Command(CommandName name, List<Integer> inputs) {
        this.name = name;
        this.inputs = inputs;
    }

    CommandName name() { return this.name; }
    List<Integer> inputs() { return this.inputs; }

    @Override
    public String toString() { return "Command name: " + name.toString() + ", Inputs: " + inputs.toString(); }

    /**
     * Given a command name and a set of inputs, produce a Command.
     * @param name The name of the command (as a CommandName).
     * @param inputs The inputs the command uses for execution.
     * @return The Command.
     */
    public static Command of(CommandName name, List<Integer> inputs) { return new Command(name, inputs); }

}

/**
 * A Command is one of the following Strings:
 * - RM1
 * - RM2
 * - RMWD
 * - DD
 * - Q
 * Interpretation: the Enum defines all possible command interfaces of the model
 */
enum CommandName {
    rm1, rm2, rmwd, dd, q;

    public static boolean isValid(String command) {
        try {
            CommandName.valueOf(command);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
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

    // TODO -- should this be moved to a CommandExecutor?
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
    public static void askForAdditionalInputs(PyramidSolitaireView view) {
        try {
            view.askForInput();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to ask for additional inputs" + e);
        }
    }
}

class CommandExtractionPackage<U> {
    U data;
    Request request;
    CommandExtractionPackage(U data, Request request) {
        this.data = data;
        this.request = request;
    }

    public U data() { return data; }
    public Request request() { return request; }

    public static <U> CommandExtractionPackage<U> of(U commandExecutor, Request request) {
        return new CommandExtractionPackage<>(commandExecutor, request);
    }
}