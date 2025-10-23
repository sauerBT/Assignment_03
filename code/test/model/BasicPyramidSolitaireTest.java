package model;

import org.junit.*;
import cs3500.pyramidsolitaire.model.hw02.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

public class BasicPyramidSolitaireTest {
    // Example Deck
    IDeck<Card> DOC52;

    // Example builders
    BasicPyramidSolitaire.Builder B01;

    // Example pyramid solitaire games
    PyramidSolitaireModel<Card> PS00;
    PyramidSolitaireModel<Card> PS01;

    @Before
    public void setupTestFixture() {
        this.DOC52 = new DeckOfCards(52);
        this.PS00 = new BasicPyramidSolitaire();
        this.B01 = BasicPyramidSolitaire.builder();
        this.PS01 = this.B01.build();
    }

    // -------------------------------------
    // getDeck()
    // -------------------------------------

    @Test
    public void getDeckTest() {
        assertEquals(new DeckOfCards(52).toList(), PS00.getDeck());
        PS00.startGame(new DeckOfCards(52).toList(), false, 7, 2);
        assertEquals(new ArrayList<>(), PS00.getDeck());
    }

    // -------------------------------------
    // startGame()
    // TODO -- missing draw card cases
    // -------------------------------------

    // Edge Case -- Empty Deck
    @Test(expected = IllegalArgumentException.class)
    public void startGameSetDeckEmpty() {
        PS00.startGame(new ArrayList<>(), false, 7, 2);
    }

    // Edge Case -- Null Deck
    @Test(expected = IllegalArgumentException.class)
    public void startGameSetDeckNull() {
        PS00.startGame(null, false, 7, 2);
    }

    // Edge Case -- Given game cannot be dealt
    @Test(expected = IllegalArgumentException.class)
    public void startGameSetDeckTooSmall() {
        PS00.startGame(new DeckOfCards(44).toList(), false, 9, 2);

    }

    // Edge Case -- Given game cannot be dealt
    @Test(expected = IllegalArgumentException.class)
    public void startGameSetNumOfRowsTooLarge() {
        PS00.startGame(DOC52.toList(), false, 10, 2);
    }

    // Edge Case -- Invalid # of rows supplied (0 rows)
    @Test(expected = IllegalArgumentException.class)
    public void startGameSetNumOfRowsTooSmall01() {
        PS00.startGame(DOC52.toList(), false, 0, 2);
    }

    // Edge Case -- Invalid # of rows supplied (negative rows)
    @Test(expected = IllegalArgumentException.class)
    public void startGameSetNumOfRowsTooSmall02() {
        PS00.startGame(DOC52.toList(), false, -1, 2);
    }

    // Edge Case -- Invalid # of draw cards supplied (0 draw cards)
    @Test(expected = IllegalArgumentException.class)
    public void startGameSetNumOfDrawCardsTooSmall01() {
        PS00.startGame(DOC52.toList(), false, 7, 0);
    }

    // Edge Case -- Invalid # of draw cards supplied (negative draw cards)
    @Test(expected = IllegalArgumentException.class)
    public void startGameSetNumOfDrawCardsTooSmall02() {
        PS00.startGame(DOC52.toList(), false, 7, -1);
    }

    // Edge Case -- Invalid Stock/Draw size (too large)
    @Test(expected = IllegalArgumentException.class)
    public void startGameStockTooLarge01() {
        PS00.startGame(DOC52.toList(), false, 9, 8);
    }

    // Regular Cases
    @Test
    public void startGameTest() {
        PS00.startGame(DOC52.toList(), false, 7, 3);
        assertEquals(1, PS00.getRowWidth(0));
        assertEquals(4, PS00.getRowWidth(3));
        assertEquals(7, PS00.getRowWidth(6));
        assertEquals(new Card(CardType.Two, Suit.Spade), PS00.getCardAt(0, 0));
        assertEquals(new Card(CardType.Seven, Suit.Spade), PS00.getCardAt(2,2));
        assertEquals(new Card(CardType.Three, Suit.Diamond), PS00.getCardAt(6,6));
        assertFalse(PS00.isGameOver());
        assertEquals(7, PS00.getNumRows());
        assertEquals(3, PS00.getNumDraw());
    }

    // -------------------------------------
    // getNumRows()
    // -------------------------------------

    @Test
    public void getNumRowsTest() {
        assertEquals(-1, PS00.getNumRows()); // Gaame not started test
        PS00.startGame(DOC52.toList(), false, 7, 2);
        assertEquals(7, PS00.getNumRows());
        assertEquals(-1, B01.pyramid(5).build().getNumRows());
        PyramidSolitaireModel<Card> builderTest = B01.pyramid(5).build();
        builderTest.startGame(DOC52.toList(), false, 5, 2);
        assertEquals(5, builderTest.getNumRows());
    }

    // -------------------------------------
    // getRowWidth()
    // -------------------------------------

    // Regular cases
    // 1. changing number of elements in rows from initial state (requires the remove function)
    // 2. Initial game state widths -- Good
    @Test
    public void getRowWidth() {
        PS00 = new BasicPyramidSolitaire();
        PS00.startGame(DOC52.toList(), false, 7, 2);
        assertEquals(7, PS00.getRowWidth(6));
        assertEquals(1, PS00.getRowWidth(0));
        assertEquals(3, PS00.getRowWidth(2));
        PS00.remove(6,3);
        assertEquals(6, PS00.getRowWidth(6));
        PS00.remove(6, 0, 6, 6);
        assertEquals(4, PS00.getRowWidth(6));
    }

    // Edge case -- Row never existed
    @Test(expected = IllegalArgumentException.class)
    public void getRowWidthIllegalRowTest01() {
        PS00.startGame(DOC52.toList(), false, 7, 2);
        PS00.getRowWidth(7);
    }

    // Edge case -- Row no longer exists due to flow of game
    @Test(expected = IllegalArgumentException.class)
    public void getRowWidthIllegalRowTest02() {
        PS00.startGame(DOC52.toList(), false, 7, 2);
        PS00.remove(6, 0, 6, 6);
        PS00.remove(6, 1, 6, 5);
        PS00.remove(6, 2, 6, 4);
        PS00.remove(6, 3);
        PS00.getRowWidth(6);
    }

    // Edge case - Game not started
    @Test(expected = IllegalStateException.class)
    public void getRowWidthGameNotStartedTest() { PS00.getRowWidth(6); }

    // -------------------------------------
    // getDrawCards()
    // -------------------------------------
    // Regular cases
    // 1. Getting draw cards at the start of the game (where number draw cards == number initial draw cards) -- Good
    // 2. Getting draw cards during game flow when draw cards have been removed
    @Test
    public void getDrawCardsTest() {
        PS00.startGame(DOC52.toList(), false, 7, 3);
        assertEquals(new ArrayList<>(List.of(
                new Card(CardType.Four, Suit.Diamond),
                new Card(CardType.Five, Suit.Diamond),
                new Card(CardType.Six, Suit.Diamond))),
                PS00.getDrawCards());
        PS00.remove(6, 0, 6, 6);
        PS00.remove(6, 1, 6, 5);
        PS00.removeUsingDraw(0, 5, 5);
        assertEquals(new ArrayList<>(List.of(
                        new Card(CardType.Five, Suit.Diamond),
                        new Card(CardType.Six, Suit.Diamond),
                        new Card(CardType.Seven, Suit.Diamond))),
                PS00.getDrawCards());
        PS00.remove(6, 2, 6, 4);
        PS00.removeUsingDraw(0, 5, 4);
        assertEquals(new ArrayList<>(
                List.of(
                        new Card(CardType.Six, Suit.Diamond),
                        new Card(CardType.Seven, Suit.Diamond),
                        new Card(CardType.Eight, Suit.Diamond))),
                PS00.getDrawCards());

    }

    // Edge Case:
    // Game has not yet started
    @Test(expected = IllegalStateException.class)
    public void getDrawCardsGameNotStartedTest01() { B01.build().getDrawCards(); }

    // Edge Case:
    // Game has not yet started
    @Test(expected = IllegalStateException.class)
    public void getDrawCardsGameNotStartedTest02() { new BasicPyramidSolitaire().getDrawCards(); }

    // -------------------------------------
    // isGameOver()
    // -------------------------------------

    // Regular case:
    // Game Score == 0
    @Test
    public void isGameOverTest01() {
        PS00.startGame(DOC52.toList(), false, 7, 2);
        assertFalse(PS00.isGameOver());
        PS00.remove(6, 3);
        assertFalse(PS00.isGameOver());
        PS00.remove(6, 0, 6, 6);
        assertFalse(PS00.isGameOver());
        PS00.remove(6, 1, 6, 5);
        assertFalse(PS00.isGameOver());
        PS00.remove(6, 2, 6, 4);
        assertFalse(PS00.isGameOver());
        PS00.remove(5, 0, 5, 5);
        assertFalse(PS00.isGameOver());
        PS00.remove(5, 1, 5, 4);
        assertFalse(PS00.isGameOver());
        PS00.remove(5, 2, 5, 3);
        assertFalse(PS00.isGameOver());
        PS00.remove(4, 0, 4, 2);
        assertFalse(PS00.isGameOver());
        PS00.remove(4, 1); // <-- As far as you can go without starting to use the draw
        assertFalse(PS00.isGameOver());
        PS00.removeUsingDraw(0, 3, 1);
        assertFalse(PS00.isGameOver());
        PS00.removeUsingDraw(0, 3, 0);
        assertFalse(PS00.isGameOver());
        PS00.discardDraw(0);
        assertFalse(PS00.isGameOver());
        PS00.removeUsingDraw(1, 2, 0);
        assertFalse(PS00.isGameOver());
        PS00.discardDraw(1);
        assertFalse(PS00.isGameOver());
        PS00.removeUsingDraw(1, 4, 4);
        assertFalse(PS00.isGameOver());
        PS00.removeUsingDraw(1, 4, 3);
        assertFalse(PS00.isGameOver());
        PS00.discardDraw(1);
        assertFalse(PS00.isGameOver());
        PS00.discardDraw(1);
        assertFalse(PS00.isGameOver());
        PS00.discardDraw(1);
        assertFalse(PS00.isGameOver());
        PS00.removeUsingDraw(1, 3, 3);
        assertFalse(PS00.isGameOver());
        PS00.removeUsingDraw(1, 3, 2);
        assertFalse(PS00.isGameOver());
        PS00.remove(2, 1, 2, 2);
        assertFalse(PS00.isGameOver());
        PS00.discardDraw(0);
        assertFalse(PS00.isGameOver());
        PS00.discardDraw(0);
        assertFalse(PS00.isGameOver());
        PS00.discardDraw(0);
        assertFalse(PS00.isGameOver());
        PS00.discardDraw(0);
        assertFalse(PS00.isGameOver());
        PS00.discardDraw(0);
        assertFalse(PS00.isGameOver());
        PS00.removeUsingDraw(1, 1, 1);
        assertFalse(PS00.isGameOver());
        PS00.removeUsingDraw(1, 1, 0);
        assertFalse(PS00.isGameOver());
        PS00.removeUsingDraw(1, 0, 0); // <-- Game Over
        assertTrue(PS00.isGameOver());
    }

    // TODO Regular case:
    // Stock empty, no more moves
    @Test
    public void isGameOverTest02() {
        PS00.startGame(DOC52.toList(), false, 7, 2);
        assertFalse(PS00.isGameOver());
        PS00.remove(6, 3);
        PS00.remove(6, 0, 6, 6);
        PS00.remove(6, 1, 6, 5);
        PS00.remove(6, 2, 6, 4);
        PS00.remove(5, 0, 5, 5);
        PS00.remove(5, 1, 5, 4);
        PS00.remove(5, 2, 5, 3);
        PS00.remove(4, 0, 4, 2);
        PS00.remove(4, 1); // <-- As far as you can go without starting to use the draw
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        assertFalse(PS00.isGameOver());
        PS00.discardDraw(0);
        assertTrue(PS00.isGameOver());
    }

    // Edge case:
    // 1. The game has not yet been started.
    @Test(expected = IllegalStateException.class)
    public void isGameOverGameNotStartedTest01() { B01.build().isGameOver(); }

    // Edge case:
    // 1. The game has not yet been started.
    @Test(expected = IllegalStateException.class)
    public void isGameOverGameNotStartedTest02() { new BasicPyramidSolitaire().isGameOver(); }

    // -------------------------------------
    // getScore()
    // -------------------------------------

    // Regular Cases
    // 1. Initial game score
    // 2. Flow of game score - remove 2
    // 3. Flow of game score - remove 1
    // 4. Flow of game score - remove using Draw
    @Test
    public void getScoreTest() {
        PS00.startGame(new DeckOfCards(52).toList(), false, 7, 2);
        assertEquals( 187, PS00.getScore());
        PS00.remove(6, 0, 6, 6);
        assertEquals( 174, PS00.getScore());
        PS00.remove(6, 1, 6, 5);
        assertEquals( 161, PS00.getScore());
        PS00.removeUsingDraw(0, 5, 5);
        assertEquals( 152, PS00.getScore());
        PS00.remove(6, 2, 6, 4);
        assertEquals( 139, PS00.getScore());
        PS00.removeUsingDraw(0, 5, 4); // TODO -- The draw index used assume that successful removeUsingDraw() does NOT automatically "turnOver" a card from Stock
        assertEquals( 131, PS00.getScore());
    }

    // Edge Cases -- Game has not started
    @Test(expected = IllegalStateException.class)
    public void getScoreGameNotStartedTest() { PS00.getScore(); }

    // -------------------------------------
    // remove()
    // TODO Currently, remove DOES NOT check vertex edges for vertex removal... Where should this be done?
    // -------------------------------------

    // Regular Cases
    @Test(expected = IllegalArgumentException.class)
    public void removeTwoTest01() {
        PS00 = new BasicPyramidSolitaire();
        PS00.startGame(DOC52.toList(), false, 7, 2);
        assertEquals(7, PS00.getRowWidth(6));
        assertEquals(new Card(CardType.Ten, Suit.Heart), PS00.getCardAt(6,0));
        assertEquals(new Card(CardType.Three, Suit.Diamond), PS00.getCardAt(6,6));
        PS00.remove(6, 0, 6, 6);
        assertEquals(5, PS00.getRowWidth(6));
        PS00.getCardAt(6,0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeTwoTest02() {
        PS00 = new BasicPyramidSolitaire();
        PS00.startGame(DOC52.toList(), false, 7, 2);
        assertEquals(7, PS00.getRowWidth(6));
        assertEquals(new Card(CardType.Ten, Suit.Heart), PS00.getCardAt(6,0));
        assertEquals(new Card(CardType.Three, Suit.Diamond), PS00.getCardAt(6,6));
        PS00.remove(6, 0, 6, 6);
        assertEquals(5, PS00.getRowWidth(6));
        PS00.getCardAt(6,6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeTwoTest03() {
        PS00 = new BasicPyramidSolitaire();
        PS00.startGame(DOC52.toList(), false, 7, 2);
        assertEquals(7, PS00.getRowWidth(6));
        assertEquals(new Card(CardType.Ten, Suit.Heart), PS00.getCardAt(6,0));
        assertEquals(new Card(CardType.Three, Suit.Diamond), PS00.getCardAt(6,6));
        PS00.remove(6, 0, 6, 6);
        assertEquals(5, PS00.getRowWidth(6));
        PS00.remove(6, 1, 6, 5);
        assertEquals(3, PS00.getRowWidth(6));
        assertEquals(6, PS00.getRowWidth(5));
        PS00.getCardAt(6,1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeTwoTest04() {
        PS00 = new BasicPyramidSolitaire();
        PS00.startGame(DOC52.toList(), false, 7, 2);
        assertEquals(7, PS00.getRowWidth(6));
        assertEquals(new Card(CardType.Ten, Suit.Heart), PS00.getCardAt(6,0));
        assertEquals(new Card(CardType.Three, Suit.Diamond), PS00.getCardAt(6,6));
        PS00.remove(6, 0, 6, 6);
        assertEquals(5, PS00.getRowWidth(6));
        PS00.remove(6, 1, 6, 5);
        PS00.getCardAt(6,5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeTwoTest05() {
        PS00 = new BasicPyramidSolitaire();
        PS00.startGame(DOC52.toList(), false, 7, 2);
        assertEquals(7, PS00.getRowWidth(6));
        assertEquals(new Card(CardType.Ten, Suit.Heart), PS00.getCardAt(6,0));
        assertEquals(new Card(CardType.Three, Suit.Diamond), PS00.getCardAt(6,6));
        PS00.remove(6, 0, 6, 6);
        assertEquals(5, PS00.getRowWidth(6));
        PS00.remove(6, 1, 6, 5);
        assertEquals(3, PS00.getRowWidth(6));
        assertEquals(6, PS00.getRowWidth(5));
        PS00.remove(5, 0, 5, 5);
        assertEquals(3, PS00.getRowWidth(6));
        assertEquals(4, PS00.getRowWidth(5));
        PS00.getCardAt(5,0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeTwoTest06() {
        PS00 = new BasicPyramidSolitaire();
        PS00.startGame(DOC52.toList(), false, 7, 2);
        assertEquals(7, PS00.getRowWidth(6));
        assertEquals(new Card(CardType.Ten, Suit.Heart), PS00.getCardAt(6,0));
        assertEquals(new Card(CardType.Three, Suit.Diamond), PS00.getCardAt(6,6));
        PS00.remove(6, 0, 6, 6);
        assertEquals(5, PS00.getRowWidth(6));
        PS00.remove(6, 1, 6, 5);
        assertEquals(3, PS00.getRowWidth(6));
        assertEquals(6, PS00.getRowWidth(5));
        PS00.remove(5, 0, 5, 5);
        assertEquals(3, PS00.getRowWidth(6));
        assertEquals(4, PS00.getRowWidth(5));
        PS00.getCardAt(5,5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeTwoTest07() {
        PS00 = new BasicPyramidSolitaire();
        PS00.startGame(DOC52.toList(), false, 7, 2);
        assertEquals(7, PS00.getRowWidth(6));
        assertEquals(new Card(CardType.Ten, Suit.Heart), PS00.getCardAt(6,0));
        assertEquals(new Card(CardType.Three, Suit.Diamond), PS00.getCardAt(6,6));
        PS00.remove(6, 0, 6, 6);
        assertEquals(5, PS00.getRowWidth(6));
        PS00.remove(6, 1, 6, 5);
        assertEquals(3, PS00.getRowWidth(6));
        assertEquals(6, PS00.getRowWidth(5));
        PS00.remove(5, 0, 5, 5);
        assertEquals(3, PS00.getRowWidth(6));
        assertEquals(4, PS00.getRowWidth(5));
        PS00.remove(6, 2, 6, 4);
        assertEquals(1, PS00.getRowWidth(6));
        assertEquals(4, PS00.getRowWidth(5));
        PS00.getCardAt(6,2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeTwoTest08() {
        PS00 = new BasicPyramidSolitaire();
        PS00.startGame(DOC52.toList(), false, 7, 2);
        assertEquals(7, PS00.getRowWidth(6));
        assertEquals(new Card(CardType.Ten, Suit.Heart), PS00.getCardAt(6,0));
        assertEquals(new Card(CardType.Three, Suit.Diamond), PS00.getCardAt(6,6));
        PS00.remove(6, 0, 6, 6);
        assertEquals(5, PS00.getRowWidth(6));
        PS00.remove(6, 1, 6, 5);
        assertEquals(3, PS00.getRowWidth(6));
        assertEquals(6, PS00.getRowWidth(5));
        PS00.remove(5, 0, 5, 5);
        assertEquals(3, PS00.getRowWidth(6));
        assertEquals(4, PS00.getRowWidth(5));
        PS00.remove(6, 2, 6, 4);
        assertEquals(1, PS00.getRowWidth(6));
        assertEquals(4, PS00.getRowWidth(5));
        PS00.getCardAt(6,4);
    }

    // Edge Case
    // Move is invalid, value != 13
    @Test(expected = IllegalArgumentException.class)
    public void removeTwoInvalidMoveTest01() {
        PS00 = new BasicPyramidSolitaire();
        PS00.startGame(DOC52.toList(), false, 7, 2);
        PS00.remove(6, 0, 6, 1);
    }

    // Edge Case
    // Move is invalid, 1 cards partially covered
    @Test(expected = IllegalArgumentException.class)
    public void removeTwoInvalidMoveTest02() {
        PS00 = new BasicPyramidSolitaire();
        PS00.startGame(DOC52.toList(), false, 7, 2);
        System.out.println(PS00.getCardAt(4, 4));
        PS00.remove(6, 0, 4, 4);
    }

    // Edge Case
    // Move is invalid, both cards partially covered
    @Test(expected = IllegalArgumentException.class)
    public void removeTwoInvalidMoveTest03() {
        PS00 = new BasicPyramidSolitaire();
        PS00.startGame(DOC52.toList(), false, 7, 2);
        PS00.remove(5, 5, 5, 0);
    }

    // Edge Case
    // Game not started
    @Test(expected = IllegalStateException.class)
    public void removeTwoGameNotStartedTest() {
        PS00 = new BasicPyramidSolitaire();
        PS00.remove(4, 1, 4, 2); // Card value allows removal but the card is covered
    }

    // Regular Cases
    @Test(expected = IllegalArgumentException.class)
    public void removeOneTest() {
        PS00 = new BasicPyramidSolitaire();
        PS00.startGame(DOC52.toList(), false, 7, 2);
        assertEquals(7, PS00.getRowWidth(6));
        assertEquals(new Card(CardType.King, Suit.Heart), PS00.getCardAt(6,3));
        PS00.remove(6, 3);
        assertEquals(6, PS00.getRowWidth(6));
        Card card = PS00.getCardAt(6,3);
    }

    // Edge Case
    // Move is invalid, value != 13
    @Test(expected = IllegalArgumentException.class)
    public void removeOneInvalidMoveTest01() {
        PS00 = new BasicPyramidSolitaire();
        PS00.startGame(DOC52.toList(), false, 7, 2);
        PS00.remove(7, 0);
    }

    // Edge Case
    // Move is invalid, card partially covered
    @Test(expected = IllegalArgumentException.class)
    public void removeOneInvalidMoveTest02() {
        PS00 = new BasicPyramidSolitaire();
        PS00.startGame(DOC52.toList(), false, 7, 2);
        PS00.remove(4, 1); // Card value allows removal but the card is covered
    }

    // Edge Case
    // Game not started
    @Test(expected = IllegalStateException.class)
    public void removeOneGameNotStartedTest() {
        PS00 = new BasicPyramidSolitaire();
        PS00.remove(4, 1); // Card value allows removal but the card is covered
    }

    // -------------------------------------
    // removeUsingDraw()
    // -------------------------------------

    // Regular Cases
    @Test(expected = IllegalArgumentException.class)
    public void removeUsingDrawTest01() {
        PS00.startGame(DOC52.toList(), false, 7, 2);
        PS00.remove(6, 0, 6, 6);
        PS00.remove(6, 1, 6, 5);
        assertEquals(6, PS00.getRowWidth(5));
        assertEquals(new Card(CardType.Nine, Suit.Heart), PS00.getCardAt(5,5));
        assertEquals(
                new ArrayList<>(List.of(
                        new Card(CardType.Four, Suit.Diamond),
                        new Card(CardType.Five, Suit.Diamond))),
                PS00.getDrawCards());
        PS00.removeUsingDraw(0, 5, 5);
        assertEquals(5, PS00.getRowWidth(5));
        assertEquals(new ArrayList<>(List.of(new Card(CardType.Five, Suit.Diamond), new Card(CardType.Six, Suit.Diamond))), PS00.getDrawCards());
        Card card = PS00.getCardAt(5,5);
    }

    // Regular Cases
    @Test(expected = IllegalArgumentException.class)
    public void removeUsingDrawTest02() {
        PS00.startGame(DOC52.toList(), false, 7, 2);
        PS00.remove(6, 0, 6, 6);
        PS00.remove(6, 1, 6, 5);
        PS00.removeUsingDraw(0, 5, 5);
        PS00.remove(6, 2, 6, 4);
        assertEquals(5, PS00.getRowWidth(5));
        assertEquals(new ArrayList<>(
                List.of(
                        new Card(CardType.Five, Suit.Diamond),
                        new Card(CardType.Six, Suit.Diamond))),
                PS00.getDrawCards());
        assertEquals(new Card(CardType.Eight, Suit.Heart), PS00.getCardAt(5,4));
        PS00.removeUsingDraw(0, 5, 4);
        assertEquals(4, PS00.getRowWidth(5));
        assertEquals(new ArrayList<>(
                List.of(
                        new Card(CardType.Six, Suit.Diamond),
                        new Card(CardType.Seven, Suit.Diamond))),
                PS00.getDrawCards());
        Card card = PS00.getCardAt(5,4);
    }

    // Edge Case - Game not started
    @Test(expected = IllegalStateException.class)
    public void removeUsingDrawGameNotStartedTest() { PS00.removeUsingDraw(0, 7, 0);}

    // Edge Case - Points do not add to 13
    @Test(expected = IllegalStateException.class)
    public void removeUsingDrawIllegalPointsRemovalTest() {
        PS00.startGame(DOC52.toList(), false, 7, 2);
        PS00.removeUsingDraw(0, 6, 0);
    }

    // Edge Case - Draw index does not exist
    @Test(expected = IllegalArgumentException.class)
    public void removeUsingDrawIllegalDrawIndexTest() {
        PS00.startGame(DOC52.toList(), false, 7, 2);
        PS00.removeUsingDraw(2, 6, 0);
    }

    // -------------------------------------
    // getCardAt()
    // -------------------------------------

    // Regular Cases
    @Test
    public void getCardAtTest() {
        PS00 = new BasicPyramidSolitaire();
        PS00.startGame(DOC52.toList(), false, 7, 2);
        assertEquals(new Card(CardType.Ten, Suit.Heart), PS00.getCardAt(6, 0));
        assertEquals(new Card(CardType.Two, Suit.Spade), PS00.getCardAt(0, 0));
    }
    
    // Edge Case
    // Position does not exist
    @Test(expected = IllegalArgumentException.class)
    public void getCardAtInvalidPosTest01() {
        PS00 = new BasicPyramidSolitaire();
        PS00.startGame(DOC52.toList(), false, 7, 2);
        PS00.getCardAt(6, 7);
    }

    // Edge Case
    // Position does not exist
    @Test(expected = IllegalArgumentException.class)
    public void getCardAtInvalidPosTest02() {
        PS00 = new BasicPyramidSolitaire();
        PS00.startGame(DOC52.toList(), false, 7, 2);
        PS00.getCardAt(6, -1);
    }

    // Edge Case
    // Card at row/position was removed
    @Test(expected = IllegalArgumentException.class)
    public void getCardAtInvalidPosTest03() {
        PS00 = new BasicPyramidSolitaire();
        PS00.startGame(DOC52.toList(), false, 7, 2);
        PS00.remove(6,3);
        PS00.getCardAt(6, 3);
    }

    // Edge Case
    // Row does not exist
    @Test(expected = IllegalArgumentException.class)
    public void getCardAtInvalidRowTest01() {
        PS00 = new BasicPyramidSolitaire();
        PS00.startGame(DOC52.toList(), false, 7, 2);
        PS00.getCardAt(7, 0);
    }

    // Edge Case
    // Row does not exist
    @Test(expected = IllegalArgumentException.class)
    public void getCardAtInvalidRowTest02() {
        PS00 = new BasicPyramidSolitaire();
        PS00.startGame(DOC52.toList(), false, 7, 2);
        PS00.getCardAt(-1, 0);
    }

    // Edge Case
    // Game not started
    @Test(expected = IllegalStateException.class)
    public void getCardAtGameNotStartedTest() {
        PS00 = new BasicPyramidSolitaire();
        PS00.getCardAt(6, 0);
    }

    // -------------------------------------
    // getNumDraw()
    // -------------------------------------
    // TODO -- do we need to account for the number of draw cards decreasing?
    @Test
    public void getNumDrawTest() {
        assertEquals(-1, PS00.getNumDraw());
        PS00.startGame(DOC52.toList(), false, 7, 2);
        assertEquals(2, PS00.getNumDraw());
        assertEquals(-1, B01.sideDeck(3).build().getNumDraw());
        PyramidSolitaireModel<Card> PS01 = B01.sideDeck(3).build();
        PS01.startGame(DOC52.toList(), false, 7, 3);
        assertEquals(3, PS01.getNumDraw());
    }

    // -------------------------------------
    // discardDraw()
    // -------------------------------------

    // Regular Case
    @Test
    public void discardDraw01() {
        PS00.startGame(DOC52.toList(), false, 7, 2);
        assertEquals(
                new ArrayList<>(List.of(
                        new Card(CardType.Four, Suit.Diamond),
                        new Card(CardType.Five, Suit.Diamond))),
                PS00.getDrawCards());
        PS00.discardDraw(0);
        assertEquals(new ArrayList<>(List.of(new Card(CardType.Five, Suit.Diamond), new Card(CardType.Six, Suit.Diamond))), PS00.getDrawCards());
    }

    // Regular Case
    @Test
    public void discardDraw02() {
        PS00.startGame(DOC52.toList(), false, 7, 2);
        assertEquals(
                new ArrayList<>(List.of(
                        new Card(CardType.Four, Suit.Diamond),
                        new Card(CardType.Five, Suit.Diamond))),
                PS00.getDrawCards());
        PS00.discardDraw(1);
        assertEquals(new ArrayList<>(List.of(new Card(CardType.Four, Suit.Diamond), new Card(CardType.Six, Suit.Diamond))), PS00.getDrawCards());
    }

    // Regular Case
    @Test
    public void discardDraw03() {
        PS00.startGame(DOC52.toList(), false, 7, 2);
        assertEquals(
                new ArrayList<>(List.of(
                        new Card(CardType.Four, Suit.Diamond),
                        new Card(CardType.Five, Suit.Diamond))),
                PS00.getDrawCards());
        PS00.discardDraw(0);
        assertEquals(new ArrayList<>(List.of(new Card(CardType.Five, Suit.Diamond), new Card(CardType.Six, Suit.Diamond))), PS00.getDrawCards());
        PS00.discardDraw(0);
        assertEquals(new ArrayList<>(List.of(new Card(CardType.Six, Suit.Diamond), new Card(CardType.Seven, Suit.Diamond))), PS00.getDrawCards());
    }

    // Regular Case
    // Stock and draw pile exhausted
    @Test
    public void discardDraw04() {
        PS00.startGame(DOC52.toList(), false, 7, 2);
        assertEquals(
                new ArrayList<>(List.of(
                        new Card(CardType.Four, Suit.Diamond),
                        new Card(CardType.Five, Suit.Diamond))),
                PS00.getDrawCards());
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        assertEquals(new ArrayList<>(List.of(new Card(CardType.King, Suit.Club), new Card(CardType.Ace, Suit.Club))), PS00.getDrawCards());
        PS00.discardDraw(0);
        assertEquals(new ArrayList<>(List.of(new Card(CardType.Ace, Suit.Club))), PS00.getDrawCards());
        PS00.discardDraw(0);
        assertEquals(new ArrayList<>(), PS00.getDrawCards());
    }

    // Edge Case
    // Game not started
    @Test(expected = IllegalStateException.class)
    public void discardDrawGameNotStarted() { PS00.discardDraw(0); }

    // Edge Case
    // Empty draw pile
    @Test(expected = IllegalArgumentException.class)
    public void discardDrawEmptyDrawPile() {
        PS00.startGame(DOC52.toList(), false, 7, 0);
        PS00.discardDraw(0);
    }

    // Edge Case
    // Draw index greater than the size of the draw pile
    @Test(expected = IllegalArgumentException.class)
    public void discardDrawInvalidDrawIndex01() {
        PS00.startGame(DOC52.toList(), false, 7, 2);
        PS00.discardDraw(2);
    }

    // Edge Case
    // Draw index impossible
    @Test(expected = IllegalArgumentException.class)
    public void discardDrawInvalidDrawIndex02() {
        PS00.startGame(DOC52.toList(), false, 7, 2);
        PS00.discardDraw(-1);
    }

    // -------------------------------------
    // hashCode()
    // -------------------------------------
    @Test
    public void hashCodeTest() {
        List<Card> tempDeck = PS01.getDeck().reversed(); // Change the order of the Deck to invoke a different hash code
        assertEquals(Objects.hash(7, 3, PS01.getDeck()), PS01.hashCode());
        assertNotEquals(Objects.hash(7, 2, PS01.getDeck()), PS01.hashCode());
        assertNotEquals(Objects.hash(6, 3, PS01.getDeck()), PS01.hashCode());
        assertNotEquals(Objects.hash(7, 3, tempDeck), PS01.hashCode());
    }

    // -------------------------------------
    // equals()
    // -------------------------------------
    @Test
    public void equalsTest() {
        List<Card> expectedNotEqualDeck = new DeckOfCards(52).toList().reversed();
        BasicPyramidSolitaire.Builder builder = BasicPyramidSolitaire.builder();
        PyramidSolitaireModel<Card> expectedEqualsPS = builder.build();
        assertEquals(expectedEqualsPS, PS01);
        PyramidSolitaireModel<Card> expectedNotEqualsPS01 = builder.deck(expectedNotEqualDeck).build();
        assertNotEquals(expectedNotEqualsPS01, PS01);
        PyramidSolitaireModel<Card> expectedNotEqualsPS02 = builder.pyramid(6).build();
        assertNotEquals(expectedNotEqualsPS02, PS01);
        PyramidSolitaireModel<Card> expectedNotEqualsPS03 = builder.sideDeck(4).build();
        assertNotEquals(expectedNotEqualsPS03, PS01);
    }

    // -------------------------------------
    // toString()
    // -------------------------------------

    // Edge Case
    // Game not started
    @Test
    public void toStringNotStarted() {
        assertEquals("",  PS00.toString());
    }

    // Edge Case
    // Game over - pyramid completed
    @Test
    public void toStringComplete() {
        PS00.startGame(DOC52.toList(), false, 7, 2);
        PS00.remove(6, 3);
        PS00.remove(6, 0, 6, 6);
        PS00.remove(6, 1, 6, 5);
        PS00.remove(6, 2, 6, 4);
        PS00.remove(5, 0, 5, 5);
        PS00.remove(5, 1, 5, 4);
        PS00.remove(5, 2, 5, 3);
        PS00.remove(4, 0, 4, 2);
        PS00.remove(4, 1); // <-- As far as you can go without starting to use the draw
        PS00.removeUsingDraw(0, 3, 1);
        PS00.removeUsingDraw(0, 3, 0);
        PS00.discardDraw(0);
        PS00.removeUsingDraw(1, 2, 0);
        PS00.discardDraw(1);
        PS00.removeUsingDraw(1, 4, 4);
        PS00.removeUsingDraw(1, 4, 3);
        PS00.discardDraw(1);
        PS00.discardDraw(1);
        PS00.discardDraw(1);
        PS00.removeUsingDraw(1, 3, 3);
        PS00.removeUsingDraw(1, 3, 2);
        PS00.remove(2, 1, 2, 2);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.removeUsingDraw(1, 1, 1);
        PS00.removeUsingDraw(1, 1, 0);
        PS00.removeUsingDraw(1, 0, 0); // <-- Game Over
        assertEquals("You win!", PS00.toString());

    }

    // Edge Case
    // Game Over - pyramid not completed & out of moves
    @Test
    public void toStringNotComplete() {
        PS00.startGame(DOC52.toList(), false, 7, 2);
        PS00.remove(6, 3);
        PS00.remove(6, 0, 6, 6);
        PS00.remove(6, 1, 6, 5);
        PS00.remove(6, 2, 6, 4);
        PS00.remove(5, 0, 5, 5);
        PS00.remove(5, 1, 5, 4);
        PS00.remove(5, 2, 5, 3);
        PS00.remove(4, 0, 4, 2);
        PS00.remove(4, 1); // <-- As far as you can go without starting to use the draw
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        assertEquals("Game over. Score: 70", PS00.toString());
    }
    // Regular Case
    @Test
    public void toString01() {
        PS00.startGame(DOC52.toList(), false, 7, 2);
        String row6 = "10♥  J♥  Q♥  K♥  A♥  2♦  3♦ "; // DONE
        String row5 = "  4♥  5♥  6♥  7♥  8♥  9♥ ";    // DONE
        String row4 = "    Q♣  K♣  A♣  2♥  3♥ ";      // DONE
        String row3 = "      8♣  9♣ 10♣  J♣ ";        // DONE
        String row2 = "        5♣  6♣  7♣ ";          // DONE
        String row1 = "          3♣  4♣ ";            // DONE
        String row0 = "            2♣ ";
        String Draw = "Draw:  4♦,  5♦";
        StringBuilder sb = new StringBuilder();
        sb
                .append(row0).append("\n")
                .append(row1).append("\n")
                .append(row2).append("\n")
                .append(row3).append("\n")
                .append(row4).append("\n")
                .append(row5).append("\n")
                .append(row6).append("\n")
                .append(Draw);
        String rendered = sb.toString();
        System.out.println(rendered);
        assertEquals(rendered, PS00.toString());
    }

    // Regular Case
    @Test
    public void toString02() {
        PS00.startGame(DOC52.toList(), false, 7, 2);
        PS00.remove(6, 3);
        PS00.remove(6, 0, 6, 6);
        PS00.remove(6, 1, 6, 5);
        PS00.remove(6, 2, 6, 4);
        PS00.remove(5, 0, 5, 5);
        PS00.remove(5, 1, 5, 4);
        PS00.remove(5, 2, 5, 3);
        PS00.remove(4, 0, 4, 2);
        PS00.remove(4, 1);

        String row6 = ""; // DONE
        String row5 = "";    // DONE
        String row4 = "                2♥  3♥ ";      // DONE
        String row3 = "      8♣  9♣ 10♣  J♣ ";        // DONE
        String row2 = "        5♣  6♣  7♣ ";          // DONE
        String row1 = "          3♣  4♣ ";            // DONE
        String row0 = "            2♣ ";
        String Draw = "Draw:  4♦,  5♦";
        StringBuilder sb = new StringBuilder();
        sb
                .append(row0).append("\n")
                .append(row1).append("\n")
                .append(row2).append("\n")
                .append(row3).append("\n")
                .append(row4).append("\n")
                .append(row5).append("\n")
                .append(row6).append("\n")
                .append(Draw);
        String rendered = sb.toString();
        System.out.println(rendered);
        assertEquals(rendered, PS00.toString());
    }

    @Test
    public void toString03() {
        PS00.startGame(DOC52.toList(), false, 7, 2);
        PS00.remove(6, 3);
        PS00.remove(6, 0, 6, 6);
        PS00.remove(6, 1, 6, 5);
        PS00.remove(6, 2, 6, 4);
        PS00.remove(5, 0, 5, 5);
        PS00.remove(5, 1, 5, 4);
        PS00.remove(5, 2, 5, 3);
        PS00.remove(4, 0, 4, 2);
//        PS00.remove(4, 1);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);
        PS00.discardDraw(0);

        String row6 = ""; // DONE
        String row5 = "";    // DONE
        String row4 = "        K♣      2♥  3♥ ";      // DONE
        String row3 = "      8♣  9♣ 10♣  J♣ ";        // DONE
        String row2 = "        5♣  6♣  7♣ ";          // DONE
        String row1 = "          3♣  4♣ ";            // DONE
        String row0 = "            2♣ ";
        String Draw = "Draw:";
        StringBuilder sb = new StringBuilder();
        sb
                .append(row0).append("\n")
                .append(row1).append("\n")
                .append(row2).append("\n")
                .append(row3).append("\n")
                .append(row4).append("\n")
                .append(row5).append("\n")
                .append(row6).append("\n")
                .append(Draw);
        String rendered = sb.toString();
        System.out.println(rendered);
        assertEquals(rendered, PS00.toString());
    }
}
