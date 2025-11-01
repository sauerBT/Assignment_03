package cs3500.pyramidsolitaire.controller;

import cs3500.pyramidsolitaire.model.hw02.PyramidSolitaireModel;

import java.util.List;
import java.util.Optional;

public class PyramidSolitaireTextualController implements PyramidSolitaireController {
    Readable inStream;
    Appendable outStream;

    public PyramidSolitaireTextualController(Readable inStream, Appendable outStream) {
        if ((inStream != null) && (outStream != null)) {
            this.inStream = inStream;
            this.outStream = outStream;
        } else {
            if (inStream == null) {
                throw new IllegalArgumentException("Must provide an instream!");
            } else {
                throw new IllegalArgumentException("Must provide an outstream!");
            }
        }
    }

    // TODO
    @Override
    public <K> void playGame(PyramidSolitaireModel<K> model, List<K> deck, boolean shuffle, int numRows, int numDraw) {
        if (model != null) {
            model.startGame(deck, shuffle, numRows, numDraw);
        } else {
            throw new IllegalArgumentException("Provided model cannot be null!");
        }
    };
}
