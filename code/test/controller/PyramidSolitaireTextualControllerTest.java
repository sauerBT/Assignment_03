package controller;

import cs3500.pyramidsolitaire.controller.PyramidSolitaireController;
import cs3500.pyramidsolitaire.controller.PyramidSolitaireTextualController;
import cs3500.pyramidsolitaire.model.hw02.*;
import cs3500.pyramidsolitaire.view.PyramidSolitaireTextualView;
import cs3500.pyramidsolitaire.view.PyramidSolitaireView;
import org.junit.*;

import java.io.StringReader;

import static controller.Interaction.*;
import static controller.Interaction.prints;
import static org.junit.Assert.*;

public class PyramidSolitaireTextualControllerTest {
    PyramidSolitaireModel<Card> PSM00;
    PyramidSolitaireModel<Card> PSM01GameOverNoWin;
    PyramidSolitaireModel<Card> PSM01GameOverWin;
    PyramidSolitaireController PSC00;
    PyramidSolitaireView PSV00;

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
        PSM01GameOverNoWin = new MockPyramidSolitaireGameOverNoWinModel(actualOutput01);
        PSM01GameOverWin = new MockPyramidSolitaireGameOverWinModel(actualOutput01);
        PSV00 = new PyramidSolitaireTextualView(PSM00, actualOutput01);
    }

    void testPlayGame(PyramidSolitaireModel<Card> model, Interaction... interactions) {
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
    public void testStartGame() {
        this.testPlayGame(PSM00,
                transmitGameState(),
                transmitGameScore(),
                inputs("q\n"),
                prints("Game Quit!"),
                prints("State of the game when quit:"),
                transmitGameState(),
                transmitGameScore());
    }

    @Test
    public void testRemoveCardSingle() {
        this.testPlayGame(PSM00,
                transmitGameState(),
                transmitGameScore(),
                inputs("rm1 7 6\n"),
                prints("method = rm1, row = 7, card = 6"),
                transmitGameState(),
                transmitGameScore(),
                inputs("Q\n"),
                prints("Game Quit!"),
                prints("State of the game when quit:"),
                transmitGameState(),
                transmitGameScore());
    }

    @Test
    public void testGameOverNoWin() {
        this.testPlayGame(PSM01GameOverNoWin,
                transmitGameOverNoWin());
    }

    @Test
    public void testGameOverWin() {
        this.testPlayGame(PSM01GameOverWin,
                transmitGameOverWin());
    }

    @Test
    public void testRemoveCardDouble() {
        this.testPlayGame(PSM00,
                transmitGameState(),
                transmitGameScore(),
                inputs("rm2 7 7 7 1\n"),
                prints("method = rm2, row1 = 7, card1 = 7, row2 = 7, card2 = 1"),
                transmitGameState(),
                transmitGameScore(),
                inputs("q\n"),
                prints("Game Quit!"),
                prints("State of the game when quit:"),
                transmitGameState(),
                transmitGameScore());
    }

    @Test
    public void testRemoveCardDraw() {
        this.testPlayGame(PSM00,
                transmitGameState(),
                transmitGameScore(),
                inputs("rmwd 2 7 7\n"),
                prints("method = rmwd, drawIndex = 2, row = 7, card = 7"),
                transmitGameState(),
                transmitGameScore(),
                inputs("Q\n"),
                prints("Game Quit!"),
                prints("State of the game when quit:"),
                transmitGameState(),
                transmitGameScore());
    }

    @Test
    public void testDiscardDraw() {
        this.testPlayGame(PSM00,
                transmitGameState(),
                transmitGameScore(),
                inputs("dd 1\n"),
                prints("method = dd, drawIndex = 1"),
                transmitGameState(),
                transmitGameScore(),
                inputs("q\n"),
                prints("Game Quit!"),
                prints("State of the game when quit:"),
                transmitGameState(),
                transmitGameScore());
    }

    @Test
    public void testMultiMethods01() {
        this.testPlayGame(PSM00,
                transmitGameState(),
                transmitGameScore(),
                inputs("dd 1\n"),
                prints("method = dd, drawIndex = 1"),
                transmitGameState(),
                transmitGameScore(),
                inputs("rm1 7 7\n"),
                prints("method = rm1, row = 7, card = 7"),
                transmitGameState(),
                transmitGameScore(),
                inputs("rm2 7 7 7 1\n"),
                prints("method = rm2, row1 = 7, card1 = 7, row2 = 7, card2 = 1"),
                transmitGameState(),
                transmitGameScore(),
                inputs("rmwd 2 7 7\n"),
                prints("method = rmwd, drawIndex = 2, row = 7, card = 7"),
                transmitGameState(),
                transmitGameScore(),
                inputs("Q\n"),
                prints("Game Quit!"),
                prints("State of the game when quit:"),
                transmitGameState(),
                transmitGameScore());
    }

    @Test
    public void testMultiRm1() {
        this.testPlayGame(PSM00,
                transmitGameState(),
                transmitGameScore(),
                inputs("rm1 7 7\n"),
                prints("method = rm1, row = 7, card = 7"),
                transmitGameState(),
                transmitGameScore(),
                inputs("rm1 1 7\n"),
                prints("method = rm1, row = 1, card = 7"),
                transmitGameState(),
                transmitGameScore(),
                inputs("rm1 3 4\n"),
                prints("method = rm1, row = 3, card = 4"),
                transmitGameState(),
                transmitGameScore(),
                inputs("q\n"),
                prints("Game Quit!"),
                prints("State of the game when quit:"),
                transmitGameState(),
                transmitGameScore()
        );
    }

    @Test
    public void testMultiRm2() {
        this.testPlayGame(PSM00,
                transmitGameState(),
                transmitGameScore(),
                inputs("rm2 7 7 1 3\n"),
                prints("method = rm2, row1 = 7, card1 = 7, row2 = 1, card2 = 3"),
                transmitGameState(),
                transmitGameScore(),
                inputs("rm2 1 7 3 4\n"),
                prints("method = rm2, row1 = 1, card1 = 7, row2 = 3, card2 = 4"),
                transmitGameState(),
                transmitGameScore(),
                inputs("rm2 3 4 2 5\n"),
                prints("method = rm2, row1 = 3, card1 = 4, row2 = 2, card2 = 5"),
                transmitGameState(),
                transmitGameScore(),
                inputs("Q\n"),
                prints("Game Quit!"),
                prints("State of the game when quit:"),
                transmitGameState(),
                transmitGameScore()
        );
    }

    @Test
    public void testMultiRmwd() {
        this.testPlayGame(PSM00,
                transmitGameState(),
                transmitGameScore(),
                inputs("rmwd 1 7 7\n"),
                prints("method = rmwd, drawIndex = 1, row = 7, card = 7"),
                transmitGameState(),
                transmitGameScore(),
                inputs("rmwd 2 1 7\n"),
                prints("method = rmwd, drawIndex = 2, row = 1, card = 7"),
                transmitGameState(),
                transmitGameScore(),
                inputs("rmwd 3 3 4\n"),
                prints("method = rmwd, drawIndex = 3, row = 3, card = 4"),
                transmitGameState(),
                transmitGameScore(),
                inputs("q\n"),
                prints("Game Quit!"),
                prints("State of the game when quit:"),
                transmitGameState(),
                transmitGameScore()
        );
    }

    @Test
    public void testMultiDd() {
        this.testPlayGame(PSM00,
                transmitGameState(),
                transmitGameScore(),
                inputs("dd 2\n"),
                prints("method = dd, drawIndex = 2"),
                transmitGameState(),
                transmitGameScore(),
                inputs("dd 1\n"),
                prints("method = dd, drawIndex = 1"),
                transmitGameState(),
                transmitGameScore(),
                inputs("dd 3\n"),
                prints("method = dd, drawIndex = 3"),
                transmitGameState(),
                transmitGameScore(),
                inputs("Q\n"),
                prints("Game Quit!"),
                prints("State of the game when quit:"),
                transmitGameState(),
                transmitGameScore()
        );
    }

    @Test
    public void testSingleRetry01() {
        this.testPlayGame(PSM00,
                transmitGameState(),
                transmitGameScore(),
                inputs("rmwd 2 7\n"),
                prints("Entry not complete! Please add additional inputs!"),
                inputs("7\n"),
                prints("method = rmwd, drawIndex = 2, row = 7, card = 7"),
                transmitGameState(),
                transmitGameScore(),
                inputs("Q\n"),
                prints("Game Quit!"),
                prints("State of the game when quit:"),
                transmitGameState(),
                transmitGameScore());
    }

    @Test
    public void testSingleRetry02() {
        this.testPlayGame(PSM00,
                transmitGameState(),
                transmitGameScore(),
                inputs("rmwd 2 7 asdf\n"),
                prints("Entry not complete! Please add additional inputs!"),
                inputs("7\n"),
                prints("method = rmwd, drawIndex = 2, row = 7, card = 7"),
                transmitGameState(),
                transmitGameScore(),
                inputs("Q\n"),
                prints("Game Quit!"),
                prints("State of the game when quit:"),
                transmitGameState(),
                transmitGameScore());
    }

    @Test
    public void testSingleRetry03() {
        this.testPlayGame(PSM00,
                transmitGameState(),
                transmitGameScore(),
                inputs("rmwd 2\n"),
                prints("Entry not complete! Please add additional inputs!"),
                inputs("7 7\n"),
                prints("method = rmwd, drawIndex = 2, row = 7, card = 7"),
                transmitGameState(),
                transmitGameScore(),
                inputs("Q\n"),
                prints("Game Quit!"),
                prints("State of the game when quit:"),
                transmitGameState(),
                transmitGameScore());
    }

    @Test
    public void testSingleRetry04() {
        this.testPlayGame(PSM00,
                transmitGameState(),
                transmitGameScore(),
                inputs("rmwd 2\n"),
                prints("Entry not complete! Please add additional inputs!"),
                inputs("7\n"),
                prints("Entry not complete! Please add additional inputs!"),
                inputs("6\n"),
                prints("method = rmwd, drawIndex = 2, row = 7, card = 6"),
                transmitGameState(),
                transmitGameScore(),
                inputs("Q\n"),
                prints("Game Quit!"),
                prints("State of the game when quit:"),
                transmitGameState(),
                transmitGameScore());
    }

    @Test
    public void testSingleRetry05() {
        this.testPlayGame(PSM00,
                transmitGameState(),
                transmitGameScore(),
                inputs("rmwd 2 \n"),
                prints("Entry not complete! Please add additional inputs!"),
                inputs("7\n"),
                prints("Entry not complete! Please add additional inputs!"),
                inputs("6\n"),
                prints("method = rmwd, drawIndex = 2, row = 7, card = 6"),
                transmitGameState(),
                transmitGameScore(),
                inputs("Q\n"),
                prints("Game Quit!"),
                prints("State of the game when quit:"),
                transmitGameState(),
                transmitGameScore());
    }

    @Test
    public void testSingleRetry06() {
        this.testPlayGame(PSM00,
                transmitGameState(),
                transmitGameScore(),
                inputs("rmwd\n"),
                prints("Entry not complete! Please add additional inputs!"),
                inputs("2\n"),
                prints("Entry not complete! Please add additional inputs!"),
                inputs("7\n"),
                prints("Entry not complete! Please add additional inputs!"),
                inputs("6\n"),
                prints("method = rmwd, drawIndex = 2, row = 7, card = 6"),
                transmitGameState(),
                transmitGameScore(),
                inputs("Q\n"),
                prints("Game Quit!"),
                prints("State of the game when quit:"),
                transmitGameState(),
                transmitGameScore());
    }

    @Test
    public void testSingleRetryWithQuit() {
        this.testPlayGame(PSM00,
                transmitGameState(),
                transmitGameScore(),
                inputs("rmwd 2 7\n"),
                prints("Entry not complete! Please add additional inputs!"),
                inputs("Q\n"),
                prints("Game Quit!"),
                prints("State of the game when quit:"),
                transmitGameState(),
                transmitGameScore());
    }

    @Test
    public void testSingleRetryWithInvalidMove() {
        this.testPlayGame(PSM00,
                transmitGameState(),
                transmitGameScore(),
                inputs("rmwd 2 7 10\n"),
                prints("Invalid move. Play again. java.lang.IllegalArgumentException: Given draw index is invalid."),
                inputs("Q\n"),
                prints("Game Quit!"),
                prints("State of the game when quit:"),
                transmitGameState(),
                transmitGameScore());
    }

    @Test
    public void testMultiRetry01() {
        this.testPlayGame(PSM00,
                transmitGameState(),
                transmitGameScore(),
                inputs("dd\n"),
                prints("Entry not complete! Please add additional inputs!"),
                inputs("2\n"),
                prints("method = dd, drawIndex = 2"),
                transmitGameState(),
                transmitGameScore(),
                inputs("dd 1\n"),
                prints("method = dd, drawIndex = 1"),
                transmitGameState(),
                transmitGameScore(),
                inputs("dd 3\n"),
                prints("method = dd, drawIndex = 3"),
                transmitGameState(),
                transmitGameScore(),
                inputs("Q\n"),
                prints("Game Quit!"),
                prints("State of the game when quit:"),
                transmitGameState(),
                transmitGameScore()
        );
    }

    @Test
    public void testMultiRetry02() {
        this.testPlayGame(PSM00,
                transmitGameState(),
                transmitGameScore(),
                inputs("dd 2\n"),
                prints("method = dd, drawIndex = 2"),
                transmitGameState(),
                transmitGameScore(),
                inputs("dd 1\n"),
                prints("method = dd, drawIndex = 1"),
                transmitGameState(),
                transmitGameScore(),
                inputs("dd\n"),
                prints("Entry not complete! Please add additional inputs!"),
                inputs("3\n"),
                prints("method = dd, drawIndex = 3"),
                transmitGameState(),
                transmitGameScore(),
                inputs("Q\n"),
                prints("Game Quit!"),
                prints("State of the game when quit:"),
                transmitGameState(),
                transmitGameScore()
        );
    }

    @Test
    public void testMultiRetryWithQuit() {
        this.testPlayGame(PSM00,
                transmitGameState(),
                transmitGameScore(),
                inputs("dd 2\n"),
                prints("method = dd, drawIndex = 2"),
                transmitGameState(),
                transmitGameScore(),
                inputs("dd 1\n"),
                prints("method = dd, drawIndex = 1"),
                transmitGameState(),
                transmitGameScore(),
                inputs("dd \n"),
                prints("Entry not complete! Please add additional inputs!"),
                inputs("Q\n"),
                prints("Game Quit!"),
                prints("State of the game when quit:"),
                transmitGameState(),
                transmitGameScore()
        );
    }

    @Test
    public void testMultiRetryWithInvalidMove() {
        this.testPlayGame(PSM00,
                transmitGameState(),
                transmitGameScore(),
                inputs("dd 2\n"),
                prints("method = dd, drawIndex = 2"),
                transmitGameState(),
                transmitGameScore(),
                inputs("dd 1\n"),
                prints("method = dd, drawIndex = 1"),
                transmitGameState(),
                transmitGameScore(),
                inputs("dd 5\n"),
                prints("Invalid move. Play again. java.lang.IllegalArgumentException: Given draw index is invalid."),
                inputs("Q\n"),
                prints("Game Quit!"),
                prints("State of the game when quit:"),
                transmitGameState(),
                transmitGameScore()
        );
    }

    // TODO: Add test for invalid command retry with new inputs!!!

}
