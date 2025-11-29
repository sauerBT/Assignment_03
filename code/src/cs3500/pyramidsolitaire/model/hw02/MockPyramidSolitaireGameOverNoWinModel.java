package cs3500.pyramidsolitaire.model.hw02;

import java.util.ArrayList;
import java.util.List;

public class MockPyramidSolitaireGameOverNoWinModel implements PyramidSolitaireModel<Card> {
    StringBuilder log;
    public MockPyramidSolitaireGameOverNoWinModel(StringBuilder log) {
        this.log = log;
    }

    public List<Card> getDeck() { return new ArrayList<>(); }

    public void startGame(List<Card> deck, boolean shuffle, int numRows, int numDraw) {
        log.append(String.format("method = sg, deck = %s, shuffle = %b, numRows = %d, numDraw = %d\n", deck.toString(), shuffle, numRows, numDraw));
    }

    public void remove(int row1, int card1, int row2, int card2) throws IllegalStateException {
        log.append(String.format("method = rm2, row1 = %d, card1 = %d, row2 = %d, card2 = %d\n", row1, card1, row2, card2));
    }

    public void remove(int row, int card) throws IllegalStateException {
        log.append(String.format("method = rm1, row = %d, card = %d\n", row, card));
    }

    public void removeUsingDraw(int drawIndex, int row, int card) throws IllegalStateException {
        log.append(String.format("method = rmwd, drawIndex = %d, row = %d, card = %d\n", drawIndex, row, card));
    }

    public void discardDraw(int drawIndex) throws IllegalStateException {
        log.append(String.format("method = dd, drawIndex = %d\n", drawIndex));
    }

    public int getNumRows() { return 0; }

    public int getNumDraw() { return 0; }

    public int getRowWidth(int row) { return 0; }

    public boolean isGameOver() throws IllegalStateException { return true; }

    public int getScore() throws IllegalStateException { return 101; }

    public Card getCardAt(int row, int card) throws IllegalStateException { return new Card(CardType.Seven, Suit.Heart); }

    public List<Card> getDrawCards() throws IllegalStateException { return new ArrayList<>(); }

    public String toString() {
        if (!this.isGameStarted()) {
            return "";
        } else if (this.isGameOver() && (this.getScore() == 0)) { // game is over and pyramid is empty
            return "You win!\n";
        } else if (this.isGameOver() && (this.getScore() > 0)) { // game is over and pyramid is NOT empty
            return "Game over. Score: " + this.getScore() + "\n";
        } else {
            return "Model toString method called!\n";
        }
    }

    private boolean isGameStarted() { return true; }
}
