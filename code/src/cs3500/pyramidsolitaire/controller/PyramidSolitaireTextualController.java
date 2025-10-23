package cs3500.pyramidsolitaire.controller;

import cs3500.pyramidsolitaire.model.hw02.PyramidSolitaireModel;

import java.util.List;

public class PyramidSolitaireTextualController implements PyramidSolitaireController {
    Readable inStream;
    Appendable outStream;

    public PyramidSolitaireTextualController(Readable inStream, Appendable outStream) {
        this.inStream = inStream;
        this.outStream = outStream;
    }

    // TODO
    @Override
    public <K> void playGame(PyramidSolitaireModel<K> model, List<K> deck, boolean shuffle, int numRows, int numDraw) {};
}
