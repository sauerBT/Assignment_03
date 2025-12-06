package cs3500.pyramidsolitaire.model.hw02;

import java.util.ArrayList;
import java.util.List;

public class MockPyramidSolitaireModel implements PyramidSolitaireModel<Card> {
    StringBuilder log;
    private int rowNum;
    private int drawNum;

    public MockPyramidSolitaireModel(StringBuilder log) {
        this.log = log;
    }

    public List<Card> getDeck() { return new ArrayList<>(); }

    public void startGame(List<Card> deck, boolean shuffle, int numRows, int numDraw) {
        this.log.append(String.format("method = sg, deck = %s, shuffle = %b, numRows = %d, numDraw = %d\n", deck.toString(), shuffle, numRows, numDraw));
        this.rowNum = numRows;
        this.drawNum = numDraw;
    }

    public void remove(int row1, int card1, int row2, int card2) throws IllegalStateException {
        log.append(String.format("method = rm2, row1 = %d, card1 = %d, row2 = %d, card2 = %d\n", row1, card1, row2, card2));
    }

    public void remove(int row, int card) throws IllegalStateException {
        log.append(String.format("method = rm1, row = %d, card = %d\n", row, card));
    }

    public void removeUsingDraw(int drawIndex, int row, int card) throws IllegalStateException {
        if (this.rowNum < card || (drawIndex < 0)) {
            throw new IllegalArgumentException("Given draw index is invalid.");
        } else {
            log.append(String.format("method = rmwd, drawIndex = %d, row = %d, card = %d\n", drawIndex, row, card));
        }
    }

    public void discardDraw(int drawIndex) throws IllegalStateException {
        if (this.drawNum < drawIndex || (drawIndex < 0)) {
            throw new IllegalArgumentException("Given draw index is invalid.");
        } else {
            log.append(String.format("method = dd, drawIndex = %d\n", drawIndex));
        }
    }

    public int getNumRows() { return 0; }

    public int getNumDraw() { return 0; }

    public int getRowWidth(int row) { return 0; }

    public boolean isGameOver() throws IllegalStateException { return false; }

    public int getScore() throws IllegalStateException { return 101; }

    public Card getCardAt(int row, int card) throws IllegalStateException { return new Card(CardType.Seven, Suit.Heart); }

    public List<Card> getDrawCards() throws IllegalStateException { return new ArrayList<>(); }

    public String toString() {
        return "Model toString method called!\n";
    }
}
