package view;

<<<<<<< Updated upstream
public class PyramidSolitaireViewTest {
=======
import cs3500.pyramidsolitaire.controller.PyramidSolitaireTextualController;
import cs3500.pyramidsolitaire.model.hw02.BasicPyramidSolitaire;
import cs3500.pyramidsolitaire.model.hw02.Card;
import cs3500.pyramidsolitaire.model.hw02.DeckOfCards;
import cs3500.pyramidsolitaire.model.hw02.PyramidSolitaireModel;
import cs3500.pyramidsolitaire.view.PyramidSolitaireTextualView;
import cs3500.pyramidsolitaire.view.PyramidSolitaireView;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class PyramidSolitaireViewTest {
    PyramidSolitaireView PSV00;
    PyramidSolitaireView PSV01;
    PyramidSolitaireModel<Card> PSM01;
    StringBuilder outStream01;

    @Before
    public void setupTestFixture() {
        this.outStream01 = new StringBuilder();
        this.PSM01 = new BasicPyramidSolitaire();
        this.PSV01 = new PyramidSolitaireTextualView(this.PSM01, this.outStream01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorIllegalArgumentTest01() {
        this.PSV00 = new PyramidSolitaireTextualView(this.PSM01, null);
    }

    @Test
    public void test() throws IOException {
        this.PSV01.render();
        assertEquals("", this.outStream01.toString());

        this.PSM01.startGame(new DeckOfCards(52).toList(), false, 7, 2);
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
        String renderExpected = sb.toString();
        this.PSV01.render();
        assertEquals(renderExpected, this.outStream01.toString());
    }
>>>>>>> Stashed changes
}
