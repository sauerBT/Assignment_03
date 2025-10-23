package cs3500.pyramidsolitaire.model.hw02;

import java.util.ArrayList;
import java.util.List;

public class DeckOfCards implements IDeck<Card> {

    private final List<Card> deck;

    public DeckOfCards(int numCards) {
        this.deck = generateDeck(numCards);
    }

    /**
     * Produce a List of Card given the number of cards that should be in the list.
     *
     * @param numCards The number of cards that should be in the list.
     * @return The List of Card
     */
    private static List<Card> generateDeck(int numCards) {
        List<Card> deck = new ArrayList<Card>();
        int acc = 0;
        for (Suit suit : Suit.values()) {
            for (CardType rank : CardType.values()) {
                if (acc >= numCards) {
                    return deck;
                } else {
                    deck.add(new Card(rank, suit));
                    acc += 1;
                }
            }
        }
        return deck;
    }

    public IDeck<Card> shuffle() { return new DeckOfCards(0); }

    public List<Card> toList() { return this.deck; }

}
