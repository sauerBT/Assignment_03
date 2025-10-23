package cs3500.pyramidsolitaire.model.hw02;

import java.util.List;

public interface IDeck<K> {
    /**
     * Produce a randomly shuffled deck
     *
     * @return The shuffled Deck
     */
    IDeck<K> shuffle();

    /**
     * Produce the Deck as a List
     *
     * @return The deck as a list
     */
    List<K> toList();
}
