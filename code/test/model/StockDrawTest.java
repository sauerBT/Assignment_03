package model;

import cs3500.pyramidsolitaire.model.hw02.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.*;

public class StockDrawTest {
    // Example Deck of Cards
    List<Card> DECK52;

    // Examples of Stocks
    List<Card> S00;
    List<Card> S01;
    List<Card> S03;

    // Examples of Draws
    List<Card> D00;
    List<Card> D01;
    List<Card> D03;

    // Examples of SideDecks
    SideDeck SD00;
    SideDeck SD01;
    SideDeck SD03;

    @Before
    public void setupTestFixture() {
        // Initialize Example Deck of Cards
        DECK52 = new ArrayList<>();
        for (Suit s : Suit.values()) {
            for (CardType ct : CardType.values()) {
                DECK52.add(new Card(ct, s));
            }
        }
        System.out.println(DECK52);

        // Initialize Stock Examples
        S00 = new ArrayList<>();
        S01 = new ArrayList<>(List.of(DECK52.get(3)));
        S03 = new ArrayList<>(DECK52.subList(3, DECK52.size()));

        // Initialize Draw Examples
        D00 = new ArrayList<>();
        D01 = new ArrayList<>(List.of(DECK52.getFirst()));
        D03 = new ArrayList<>(List.of(DECK52.get(0), DECK52.get(1), DECK52.get(2)));
    }
}
