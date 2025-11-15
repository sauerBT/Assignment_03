package controller;

import cs3500.pyramidsolitaire.controller.PyramidSolitaireController;
import cs3500.pyramidsolitaire.controller.PyramidSolitaireTextualController;
import cs3500.pyramidsolitaire.model.hw02.Card;
import cs3500.pyramidsolitaire.model.hw02.DeckOfCards;
import cs3500.pyramidsolitaire.model.hw02.MockPyramidSolitaireModel;
import cs3500.pyramidsolitaire.model.hw02.PyramidSolitaireModel;
import org.junit.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import static controller.Interaction.*;
import static org.junit.Assert.*;

public class PyramidSolitaireTextualControllerTest {
    PyramidSolitaireModel<Card> PSM00;
    PyramidSolitaireController PSC00;

    StringBuilder inStreamMaker01;
    StringBuilder expectedOutput01;
    StringBuilder actualOutput01;

    StringReader inputStream01;

    @Before
    public void setupFixture() {
        inStreamMaker01 = new StringBuilder();
        expectedOutput01 = new StringBuilder();
        actualOutput01 = new StringBuilder();
        PSM00 = new MockPyramidSolitaireModel(actualOutput01);
    }

    void testPlayGame(PyramidSolitaireModel<Card> model, Interaction... interactions) throws IOException {
        expectedOutput01.append(String.format("method = sg, deck = %s, shuffle = false, numRows = 7, numDraw = 3\n", new DeckOfCards(52).toList()));
        for (Interaction interaction : interactions) {
            interaction.apply(inStreamMaker01, expectedOutput01);
        }
        inputStream01 = new StringReader(inStreamMaker01.toString());
        PSC00 = new PyramidSolitaireTextualController(inputStream01, actualOutput01);
        PSC00.playGame(model, new DeckOfCards(52).toList(), false, 7, 3);
        assertEquals(expectedOutput01.toString(), actualOutput01.toString());
    }

    @Test
    public void testStartGame() throws IOException {
        this.testPlayGame(PSM00, inputs("q\n"));
    }

    @Test
    public void testRemoveCardSingle() throws IOException {
        this.testPlayGame(PSM00,
                inputs("rm1 7 6\n"),
                prints("method = rm1, row = 7, card = 6"),
                inputs("q\n"));
    }

    @Test
    public void testRemoveCardDouble() throws IOException{
        this.testPlayGame(PSM00,
                inputs("rm2 7 7 7 1\n"),
                prints("method = rm2, row1 = 7, card1 = 7, row2 = 7, card2 = 1"),
                inputs("q\n"));
    }

    @Test
    public void testRemoveCardDraw() throws IOException{
        this.testPlayGame(PSM00,
                inputs("rmwd 2 7 7\n"),
                prints("method = rmwd, drawIndex = 2, row = 7, card = 7"),
                inputs("q\n"));
    }

    @Test
    public void testDiscardDraw() throws IOException{
        this.testPlayGame(PSM00,
                inputs("dd 1\n"),
                prints("method = dd, drawIndex = 1"),
                inputs("q\n"));
    }

    @Test
    public void testMultiMethods01() throws IOException{
        this.testPlayGame(PSM00,
                inputs("dd 1\n"),
                prints("method = dd, drawIndex = 1"),
                inputs("rm1 7 7\n"),
                prints("method = rm1, row = 7, card = 7"),
                inputs("rm2 7 7 7 1\n"),
                prints("method = rm2, row1 = 7, card1 = 7, row2 = 7, card2 = 1"),
                inputs("rmwd 2 7 7\n"),
                prints("method = rmwd, drawIndex = 2, row = 7, card = 7"),
                inputs("q\n")
                );
    }

    @Test
    public void testMultiRm1() throws IOException{
        this.testPlayGame(PSM00,
                inputs("rm1 7 7\n"),
                prints("method = rm1, row = 7, card = 7"),
                inputs("rm1 1 7\n"),
                prints("method = rm1, row = 1, card = 7"),
                inputs("rm1 3 4\n"),
                prints("method = rm1, row = 3, card = 4"),
                inputs("q\n")
        );
    }

    @Test
    public void testMultiRm2() throws IOException{
        this.testPlayGame(PSM00,
                inputs("rm2 7 7 1 3\n"),
                prints("method = rm2, row1 = 7, card1 = 7, row2 = 1, card2 = 3"),
                inputs("rm2 1 7 3 4\n"),
                prints("method = rm2, row1 = 1, card1 = 7, row2 = 3, card2 = 4"),
                inputs("rm2 3 4 2 5\n"),
                prints("method = rm2, row1 = 3, card1 = 4, row2 = 2, card2 = 5"),
                inputs("q\n")
        );
    }

    @Test
    public void testMultiRmwd() throws IOException{
        this.testPlayGame(PSM00,
                inputs("rmwd 1 7 7\n"),
                prints("method = rmwd, drawIndex = 1, row = 7, card = 7"),
                inputs("rmwd 2 1 7\n"),
                prints("method = rmwd, drawIndex = 2, row = 1, card = 7"),
                inputs("rmwd 3 3 4\n"),
                prints("method = rmwd, drawIndex = 3, row = 3, card = 4"),
                inputs("q\n")
        );
    }

    @Test
    public void testMultiDd() throws IOException{
        this.testPlayGame(PSM00,
                inputs("dd 2\n"),
                prints("method = dd, drawIndex = 2"),
                inputs("dd 1\n"),
                prints("method = dd, drawIndex = 1"),
                inputs("dd 3\n"),
                prints("method = dd, drawIndex = 3"),
                inputs("q\n")
        );
    }
}
