package cs3500.pyramidsolitaire.view;

import cs3500.pyramidsolitaire.model.hw02.PyramidSolitaireModel;

import java.io.IOException;

public class PyramidSolitaireTextualView implements PyramidSolitaireView{
    private final PyramidSolitaireModel<?> model;
    private Appendable outStream;

    public PyramidSolitaireTextualView(PyramidSolitaireModel<?> model) {
        this.model = model;
    }

    public PyramidSolitaireTextualView(PyramidSolitaireModel<?> model, Appendable outStream) {
        this.model = model;
        this.outStream = outStream;
    }

    @Override
    public void render() throws IOException {
        this.outStream.append(this.model.toString());
    };

    @Override
    public String toString() {
        return model.toString();
    }
}
