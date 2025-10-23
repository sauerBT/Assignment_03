package model;

import cs3500.pyramidsolitaire.model.hw02.*;

import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PyramidTest {
    // Example decks
    IDeck<Card> DOC0;
    IDeck<Card> DOC01;
    IDeck<Card> DOC52;
    // Example triangles
    Pyramid<Card> P00; // Empty triangle
    Pyramid<Card> P01; // Single row triangle
    Pyramid<Card> P52; // Multi-row triangle

    @Before
    public void setupTestFixture() {
        // Produce example decks
        DOC0 = new DeckOfCards(0);
        DOC01 = new DeckOfCards(1);
        DOC52 = new DeckOfCards(52);

        // Produce example triangles
        P52 = new Pyramid<>(7, DOC52.toList());
    }

    // -------------------------------------
    // coveredCards() && coveredCardsCount()
    // -------------------------------------

    // TODO
    // Edge Case
    // Given Vertex is not within the pyramid
//    @Test(expected = IllegalArgumentException.class)
//    public void coveredCardsInvalidVertex() {
//        P52.coveringCards()
//    }
//
//    // TODO
//    // Edge Case
//    // Given Vertex is not within the pyramid
//    @Test(expected = IllegalArgumentException.class)
//    public void coveredCardsCountInvalidVertex() {
//        P52
//
//    }
//
//    // TODO
//    // Regular Case
//    @Test
//    public void coveredCards() {
//        P52
//
//    }
//
//    // TODO
//    // Regular Case
//    @Test
//    public void coveredCardsCount() {
//        P52
//
//    }
}