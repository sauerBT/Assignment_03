package cs3500.pyramidsolitaire.model.hw02;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The representative object of the Stock for playing a game of Pyramid Solitaire with a standard deck of cards.
 *<p>
 *     Object used to maintain the state of the Stock and Draw, two sets of elements that are tightly coupled together
 *     in the game of Pyramid Solitaire.
 *</p>
 *
 * @author Brian Sauerborn
 * @version 1.0
 * @since 1.0
 */
public class StockDraw<K> implements SideDeck<K> {
    private final List<K> stock;
    private final List<K> draw;
    /**
     * The maximum number of elements available to the draw pile.
     *
     * @since 1.0
     */
    private final int numDraw;

    // TODO -- make private? Make public interface with "of" and "empty"?
    public StockDraw(List<K> deck, int numDraw) {
        if (!isLegalDraw(numDraw)) {
            throw new IllegalArgumentException("Invalid draw number.");
        } else if (!isLegalDeck(deck, numDraw)) {
            throw new IllegalArgumentException("Invalid deck and draw number combination.");
        } else {
            this.numDraw = numDraw;
            this.stock = generateStock(deck, numDraw);
            this.draw = generateDraw(deck, numDraw);
        }
    }

    public StockDraw(int numDraw) {
        this.numDraw = numDraw;
        this.stock = new ArrayList<>();
        this.draw = new ArrayList<>();
    }

    private StockDraw(int numDraw, List<K> stock, List<K> draw) {
        this.numDraw = numDraw;
        this.stock = stock;
        this.draw = draw;
    }

    @Override
    public int getNumDraw() { return this.numDraw; }

    @Override
    public List<K> getDraw() { return this.draw; }

    @Override
    public List<K> getStock() { return this.stock; }

    @Override
    public K getDrawElement(int drawIndex) {
        if (this.draw.size() < (drawIndex + 1) || drawIndex < 0) {
            throw new IllegalArgumentException("Given draw index is out of bounds or invalid");
        } else {
            return this.draw.get(drawIndex);
        }
    }

    // TODO
    private static boolean isLegalDraw(int numDraw) { return numDraw > 0; }

    // TODO
    private static <K> boolean isLegalDeck(List<K> deck, int numDraw) { return (deck.size() - numDraw) > 0; }

    /**
     * Produce the initial Stock for a game of pyramid solitaire.
     *
     * @param deck The initial deck provided by the game model.
     * @param numDraw The initial number of draw elements provided by the game model.
     * @return The Stock elements.
     * @param <K> The type of elements in the deck.
     */
    private static <K> List<K> generateDraw(List<K> deck, int numDraw) { return Util.ListUtil.getFirstX(deck, numDraw); }

    /**
     * Produce the initial Stock for a game of pyramid solitaire.
     *
     * @param deck The initial deck provided by the game model.
     * @param numDraw The initial number of draw elements provided by the game model.
     * @return The Draw elements
     * @param <K> The type of elements in the deck.
     */
    private static <K> List<K> generateStock(List<K> deck, int numDraw) {
        return Util.ListUtil.removeFirstX(deck, numDraw);
    }

    @Override
    public SideDeck<K> discardDraw(int drawIndex) {
        if (this.draw.isEmpty()) {
            throw new IllegalArgumentException("Draw pile is empty. No cards to remove");
        } else if (this.draw.size() < (drawIndex + 1) || (drawIndex < 0)) {
            throw new IllegalArgumentException("Given draw index is invalid.");
        } else {
            this.draw.remove(drawIndex);
            return this.turnOver();
        }
    }

    private SideDeck<K> turnOver() {
        List<K> tempDraw = Util.ListUtil.copy(this.draw);
        List<K> tempStock = Util.ListUtil.copy(this.stock);
        if (tempStock.isEmpty()) {
            return new StockDraw<>(this.numDraw, tempStock, tempDraw);
        } else {
            tempDraw.add(tempStock.getFirst());
            tempStock.removeFirst();
            return new StockDraw<>(this.numDraw, tempStock, tempDraw);
        }
    }

    @Override
    public int size() { return stock.size() + draw.size(); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StockDraw<?>)) return false;
        StockDraw<?> that = (StockDraw<?>)obj;
        return this.stock.equals(that.stock) &&
                this.draw.equals(that.draw) &&
                this.numDraw == that.numDraw;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stock, draw, numDraw);
    }

    @Override
    public String toString() {
        if (this.draw.isEmpty()) {
            return "Draw:";
        } else {
            return "Draw: " + this.deckToString();
        }
    }

    private String deckToString() {
        return this.deckToStringHelper(new StringBuilder().append(this.draw.getFirst()),
                this.draw.subList(1, this.draw.size()));
    }

    private String deckToStringHelper(StringBuilder acc, List<K> wl) {
        if (wl.isEmpty()) {
            return acc.toString();
        } else {
            return deckToStringHelper(acc.append(", ").append(wl.getFirst()), wl.subList(1, wl.size()));
        }
    }
}