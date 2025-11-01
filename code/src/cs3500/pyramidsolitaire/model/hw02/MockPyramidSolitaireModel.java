package cs3500.pyramidsolitaire.model.hw02;

import java.util.ArrayList;
import java.util.List;

public class MockPyramidSolitaireModel implements PyramidSolitaireModel<Card> {
    Appendable log;
    MockPyramidSolitaireModel(Appendable log) {
        this.log = log;
    }

    public List<Card> getDeck() { return new ArrayList<>(); }

    public void startGame(List<Card> deck, boolean shuffle, int numRows, int numDraw) {}

    public void remove(int row1, int card1, int row2, int card2) throws IllegalStateException {}

    public void remove(int row, int card) throws IllegalStateException {}

    public void removeUsingDraw(int drawIndex, int row, int card) throws IllegalStateException {}

    public void discardDraw(int drawIndex) throws IllegalStateException {}

    public int getNumRows() { return 0; }

    public int getNumDraw() { return 0; }

    public int getRowWidth(int row) { return 0; }

    public boolean isGameOver() throws IllegalStateException { return false; }

    public int getScore() throws IllegalStateException { return 0; }

    public Card getCardAt(int row, int card) throws IllegalStateException { return new Card(CardType.Seven, Suit.Heart); }

    public List<Card> getDrawCards() throws IllegalStateException { return new ArrayList<>(); }
}
