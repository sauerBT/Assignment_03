package cs3500.pyramidsolitaire.controller;

import cs3500.pyramidsolitaire.model.hw02.PyramidSolitaireModel;

import java.util.List;
import java.util.Optional;

public class PyramidSolitaireTextualController implements PyramidSolitaireController {
    Readable inStream;
    Appendable outStream;

    public PyramidSolitaireTextualController(Readable inStream, Appendable outStream) {
<<<<<<< Updated upstream
        if (inStream != null && outStream != null) {
=======
        if ((inStream != null) && (outStream != null)) {
>>>>>>> Stashed changes
            this.inStream = inStream;
            this.outStream = outStream;
        } else {
            if (inStream == null) {
<<<<<<< Updated upstream
                throw new IllegalArgumentException("No input stream supplied to controller.");
            } else {
                throw new IllegalArgumentException("No output stream supplied to controller.");
            }
        }

=======
                throw new IllegalArgumentException("Must provide an instream!");
            } else {
                throw new IllegalArgumentException("Must provide an outstream!");
            }
        }
>>>>>>> Stashed changes
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
