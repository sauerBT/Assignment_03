package cs3500.pyramidsolitaire.model.hw02;

import java.util.List;

public interface SideDeck<K> {
    /**
     * Produce the draw pile.
     *
     * @return List of draw elements.
     */
    List<K> getDraw();

    /**
     * Produce the stock pile.
     *
     * @return List of stock elements.
     */
    List<K> getStock();

    /**
     * Returns the maximum number of visible cards in the draw pile,
     * or -1 if the game hasn't been started.
     *
     * @return the number of visible cards in the draw pile
     */
    int getNumDraw();

    /**
     * Produce the element from this draw pile at the given index.
     *
     * @param drawIndex The given draw index
     * @return The element.
     * @throws IllegalArgumentException When the given draw index is invalid.
     */
    K getDrawElement(int drawIndex);

    /**
     * Produce the number of cards in this side deck.
     *
     * @return The size of the side deck.
     */
    int size();

    /**
     * Produce a new SideDeck with the card at the given DrawIndex removed from the draw pile.
     * <p>
     *     When a draw card is discarded, it should be replaced with the next card from the stock pile.
     *     Any other draw cards should not be affected.
     * </p>
     *
     * @param drawIndex The index containing the card to remove from the draw pile
     * @return A new SideDeck
     * @throws IllegalArgumentException When the given index is invalid or the draw pile is empty.
     */
    SideDeck<K> discardDraw(int drawIndex);
}
