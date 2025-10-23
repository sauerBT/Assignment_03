package model;

import cs3500.pyramidsolitaire.model.hw02.*;
import org.junit.*;

import static org.junit.Assert.*;

public class CardTest {
    // Example Card Suits
    Suit HEART = Suit.Heart;
    Suit SPADE = Suit.Spade;
    Suit DIAMOND = Suit.Diamond;
    Suit CLUB = Suit.Club;
    // Example Card Types
    CardType ACE = CardType.Ace;
    CardType TWO = CardType.Two;
    CardType THREE = CardType.Three;
    CardType FOUR = CardType.Four;
    CardType FIVE = CardType.Five;
    CardType SIX = CardType.Six;
    CardType SEVEN = CardType.Seven;
    CardType EIGHT = CardType.Eight;
    CardType NINE = CardType.Nine;
    CardType TEN = CardType.Ten;
    CardType JACK = CardType.Jack;
    CardType QUEEN = CardType.Queen;
    CardType KING = CardType.King;
    // Example Cards
    Card C01;
    Card C02;
    Card C03;
    Card C04;

    @Before
    public void setupTestFixture() {
        // Initialize Example Cards
        C01 = new Card(KING, HEART);
        C02 = new Card(QUEEN, SPADE);
        C03 = new Card(JACK, DIAMOND);
        C04 = new Card(ACE, CLUB);

    }

    @Test
    public void toStringTest() {
        assertEquals("K♥", C01.toString());
        assertEquals("Q♣", C02.toString());
        assertEquals("J♦", C03.toString());
        assertEquals("A♠", C04.toString());
        assertEquals("7♥", new Card(CardType.Seven, Suit.Heart).toString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(new Card(CardType.King, Suit.Heart).hashCode(), this.C01.hashCode());
        assertNotEquals(new Card(CardType.King, Suit.Spade).hashCode(), this.C01.hashCode());
    }

    @Test
    public void equalsTest() {
        assertTrue(this.C01.equals(new Card(CardType.King, Suit.Heart)));
        assertFalse(this.C01.equals(new Card(CardType.King, Suit.Diamond)));
        assertFalse(this.C01.equals(new Card(CardType.Queen, Suit.Heart)));
    }
}
