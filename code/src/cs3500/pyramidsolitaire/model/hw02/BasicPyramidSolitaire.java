package cs3500.pyramidsolitaire.model.hw02;

import cs3500.pyramidsolitaire.model.hw02.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The model for playing a game of Pyramid Solitaire with a standard deck of cards.
 *<p>
 *     Model used to maintain the state and enforce the rules of the pyramid solitaire game. The API for interfacing
 *     with this leverages the standard Builder Pattern for creation of a game.
 *</p>
 *
 * @author Brian Sauerborn
 * @version 1.0
 * @since 1.0
 * @see PyramidSolitaireModel
 * @see Builder
 */
public class BasicPyramidSolitaire implements PyramidSolitaireModel<Card> {
    /**
     * The state of the 'triangle' or 'pyramid' that the game is played from.
     *
     * @since 1.0
     */
    private Pyramid<Card> pyramid;
    /**
     * The state of both the 'stock' and the 'draw', which are tightly coupled together into the object 'StockDraw'
     *
     * @since 1.0
     */
    private SideDeck<Card> sideDeck;
    /**
     * The state of the deck used to initially create the game. When a game is started, this deck is distributed to
     * both the 'pyramid' and the 'sideDeck'.
     *
     * @since 1.0
     */
    private List<Card> deck;
    /**
     * The state of the game.
     *
     * @since 1.0
     */
    private PyramidSolitaireGameState state;

    /**
     * Construct a new Pyramid Solitaire game model with all null fields
     * <P>
     *     NOTE: this constructor is kept to satisfy the HW01TypeChecks file and
     *     has no functional purpose
     * </P>
     */
    public BasicPyramidSolitaire() {
        BasicPyramidSolitaire.Builder base = new BasicPyramidSolitaire.Builder();
        this.pyramid = base.pyramid;
        this.sideDeck = base.sideDeck;
        this.deck = base.deck;
        this.state = base.state;
    }

    /**
     * Construct a new Pyramid Solitaire game model with the given parameters
     *
     * @param pyramid the pyramid of elements
     * @param sideDeck the side deck containing the draw pile and the stock
     * @param deck the initial deck of cards
     * @param state the current state of the game
     */
    private BasicPyramidSolitaire(Pyramid<Card> pyramid, SideDeck<Card> sideDeck, List<Card> deck, PyramidSolitaireGameState state) {
        this.pyramid = pyramid;
        this.sideDeck = sideDeck;
        this.deck = deck;
        this.state = state;
    }

    @Override
    public List<Card> getDeck() {
        if (this.state == PyramidSolitaireGameState.Running) {
            return this.deck;
        } else {
            return new DeckOfCards(52).toList();
        }
    }

    // TODO
    private List<Card> shuffleDeck() {
        return this.deck;
    }

    @Override
    public int getNumRows() {
        if (!this.isGameStarted()) {
            return -1;
        } else {
            return this.pyramid.getNumRows();
        }
    }

    @Override
    public int getNumDraw() {
        if (!this.isGameStarted()) {
            return -1;
        } else {
            return this.sideDeck.getNumDraw();
        }
    }

    @Override
    public int getRowWidth(int row) {
        if (!isGameStarted()) {
            throw new IllegalStateException("Game has not been started.  Cannot retrieve current row width.");
        } else {
            return this.pyramid.getRowWidth(row);
        }
    }

    /**
     * Produce true if this game has started.
     *
     * @return True if the game has started, otherwise false.
     */
    private boolean isGameStarted() {
        return this.state.equals(PyramidSolitaireGameState.Running);
    }

    @Override
    public List<Card> getDrawCards() {
        if (!this.isGameStarted()) {
            throw new IllegalStateException("Game not started. Cannot get draw pile.");
        } else {
            return this.sideDeck.getDraw();
        }
    }

    @Override
    public void startGame(List<Card> deck, boolean shuffle, int numRows, int numDraw) {
        if (shuffle) {
            List<Card> shuffledDeck = this.shuffleDeck(deck);
            deck(this.generateSideDeck(this.generatePyramid(shuffledDeck, numRows), numDraw));
        } else {
            deck(this.generateSideDeck(this.generatePyramid(deck, numRows), numDraw));
        }
        this.state = PyramidSolitaireGameState.Running;
    }

    /**
     * Produce the given deck with original positions randomized.
     *
     * @param deck The deck of cards.
     * @return The shuffled deck of cards.
     */
    private List<Card> shuffleDeck(List<Card> deck) { return deck; }

    /**
     * Produce all cards (in their original order) not used to generate a game pyramid.
     * MUTATION: This method will modify the pyramid variable state for this game of pyramid solitaire.
     *
     * @param deck The deck of cards.
     * @param numRows The number of rows in the game's pyramid.
     * @return The cards not used to generate the game pyramid, in their original (given) order.
     */
    private List<Card> generatePyramid(List<Card> deck, int numRows) {
        this.pyramid(new Pyramid<>(numRows, deck)); // MUTATION: Set the pyramid variable.
        return Util.ListUtil.removeFirstX(deck, this.pyramid.size() - 1);
    }

    /**
     * Produce all cards NOT used to generate the StockDeck.
     * MUTATION: This method will modify the sideDeck variable state for this game of pyramid solitaire.
     *
     * @param deck The deck of cards.
     * @param numDraw The number of draw cards in the game's sideDeck.
     * @return The remaining cards in the deck.
     */
    private List<Card> generateSideDeck(List<Card> deck, int numDraw) {
        this.sideDeck(new StockDraw<>(deck, numDraw)); // MUTATION: Set the sideDeck variable.
        return Util.ListUtil.removeFirstX(deck, deck.size() - this.sideDeck.size());
    }

    /**
     * MUTATION: Deck variable setter. Set the deck variable to the given deck.
     *
     * @param deck The given deck.
     */
    private void deck(List<Card> deck) { this.deck = deck; }

    /**
     * MUTATION: SideDeck variable setter. Set the sideDeck variable to the given sideDeck.
     *
     * @param sideDeck The given sideDeck.
     */
    private void sideDeck(SideDeck<Card> sideDeck) { this.sideDeck = sideDeck; }

    /**
     * MUTATION: Pyramid variable setter. Set the pyramid variable to the given pyramid.
     *
     * @param pyramid The given pyramid.
     */
    private void pyramid(Pyramid<Card> pyramid) { this.pyramid = pyramid; }

    @Override
    public void remove(int row1, int card1, int row2, int card2) {
        if (!isGameStarted()) {
            throw new IllegalStateException("Cannot make any moves. Game has not started.");
        } else {
            int requestedRemovalValue = this.pyramid.getCardAt(row1, card1).getValue() + this.pyramid.getCardAt(row2, card2).getValue();
            if (requestedRemovalValue == 13) {
                this.pyramid(this.pyramid.removeElement(row1, card1).removeElement(row2, card2)); // MUTATION: mutates the pyramid in place
            } else {
                throw new IllegalArgumentException("Move is invalid, combined card values equal " + requestedRemovalValue);
            }
        }
    }

    @Override
    public void remove(int row, int card) {
        if (!isGameStarted()) {
            throw new IllegalStateException("Cannot make any moves. Game has not started.");
        } else {
            int requestedRemovalValue = this.pyramid.getCardAt(row, card).getValue();
            if (requestedRemovalValue == 13) {
                this.pyramid(this.pyramid.removeElement(row, card)); // MUTATION: mutates the pyramid in place
            } else {
                throw new IllegalStateException("Move is invalid, card value equals " + requestedRemovalValue);
            }
        }
    }

    @Override
    public void removeUsingDraw(int drawIndex, int row, int card) {
        if (!isGameStarted()) {
            throw new IllegalStateException("Cannot make any moves. Game has not started.");
        } else {
            int requestedRemovalValue = this.pyramid.getCardAt(row, card).getValue() + this.sideDeck.getDrawElement(drawIndex).getValue();
            if (requestedRemovalValue == 13) {
                this.pyramid(this.pyramid.removeElement(row, card)); // MUTATION: mutates the pyramid in place
                this.discardDraw(drawIndex); // MUTATION: mutates the side deck in place
            } else {
                throw new IllegalStateException("Move is invalid, card value equals " + requestedRemovalValue);
            }
        }
    }

    @Override
    public void discardDraw(int drawIndex) {
        if (!isGameStarted()) {
            throw new IllegalStateException("Cannot make any moves. Game has not started.");
        } else {
            this.sideDeck(this.sideDeck.discardDraw(drawIndex)); // MUTATION: set new side deck in place
        }
    }

    @Override
    public Card getCardAt(int row, int card) throws IllegalStateException {
        if (!isGameStarted()) {
            throw new IllegalStateException("Cannot get Card. Game not started.");
        } else {
            return this.pyramid.getCardAt(row, card);
        }
    }

    // TODO -- simplify
    @Override
    public boolean isGameOver() {
        if (!isGameStarted()) {
            throw new IllegalStateException("Game has not started!");
        } else {
            if (this.getScore() == 0) { // <-- Score is 0 (empty pyramid)
                return true;
            } else if (this.sideDeck.getStock().isEmpty()) { // <-- Stock is empty
                List<Card> uncoveredCards = Util.GameUtil.extractCard(this.pyramid.getUncoveredCards());
                if (!Util.GameUtil.isMove(this.getDrawCards(), uncoveredCards)) { // <-- No possible move combination
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public int getScore() {
        if (!this.isGameStarted()) {
            throw new IllegalStateException("Game not started.  No score available.");
        } else {
            return Util.GameUtil.getCardTotalValue(
                    Util.GameUtil.extractCard(this.pyramid.extractIPair()));
        }
    }

    @Override
    public String toString() {
        if (!this.isGameStarted()) {
            return "";
        } else if (this.isGameOver() && (this.getScore() == 0)) { // game is over and pyramid is empty
            return "You win!";
        } else if (this.isGameOver() && (this.getScore() > 0)) { // game is over and pyramid is NOT empty
            return "Game over. Score: " + this.getScore();
        } else {
            return this.pyramid.toString() + "\n" +
                    this.sideDeck.toString();
        }
    }

    // TODO
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BasicPyramidSolitaire)) return false;
        BasicPyramidSolitaire that = (BasicPyramidSolitaire)obj;
        return this.deck.equals(that.deck) &&
                this.pyramid.equals(that.pyramid) &&
                this.sideDeck.equals(that.sideDeck) &&
                this.state.equals(that.state);
    }

    // TODO
    @Override
    public int hashCode() { return Objects.hash(this.pyramid, this.sideDeck, this.state, this.deck); }
    
    /**
     * Constructs a builder for configuring and creating a game model instance. Defaults to a standard game with ...
     *
     * @return the new builder
     */
    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private final Pyramid<Card> pyramid;
        private final SideDeck<Card> sideDeck;
        private final List<Card> deck;
        private final PyramidSolitaireGameState state;

        /**
         * Constructs the PyramidSolitaire Builder with its DEFAULT values
         */
        public Builder() {
            this.pyramid = new Pyramid<>(7);
            this.sideDeck = new StockDraw<>(3);
            this.deck = new DeckOfCards(52).toList();
            this.state = PyramidSolitaireGameState.Idle;
        }

        /**
         * Constructs the PyramidSolitaire Builder with a set of given values
         *
         * @param pyramid the pyramid of elements
         * @param sideDeck the side deck containing the draw pile and the stock
         * @param deck the initial deck of cards
         * @param state the current state of the game
         */
        private Builder(Pyramid<Card> pyramid, SideDeck<Card> sideDeck, List<Card> deck, PyramidSolitaireGameState state) {
            this.pyramid = pyramid;
            this.sideDeck = sideDeck;
            this.deck = deck;
            this.state = state;
        }

        /**
         * Set the deck of Cards manually.
         *
         * @param deck the deck of cards
         * @return the updated Builder
         */
        public Builder deck(List<Card> deck) { return new Builder(this.pyramid, this.sideDeck, deck, this.state); }

        /**
         * Set the number of rows in the solitaire pyramid
         *
         * @param numRows the number of rows in the solitaire game
         * @return the updated Builder
         */
        public Builder pyramid(int numRows) { return new Builder(new Pyramid<>(numRows), this.sideDeck, this.deck, this.state); }

        /**
         * Set the number of face-up cards from the "stock" draw deck
         *
         * @param numDraw the number of face-up cards
         * @return the updated Builder
         */
        public Builder sideDeck(int numDraw) { return new Builder(this.pyramid, new StockDraw<>(numDraw), this.deck, this.state); }

        /**
         * Produce the specified {@link BasicPyramidSolitaire}.
         *
         * @return a new {@code BasicPyramidSolitaire}
         */
        public PyramidSolitaireModel<Card> build() { return new BasicPyramidSolitaire(this.pyramid, this.sideDeck, this.deck, this.state); }
    }
}

