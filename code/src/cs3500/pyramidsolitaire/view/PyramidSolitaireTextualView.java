package cs3500.pyramidsolitaire.view;

import cs3500.pyramidsolitaire.model.hw02.PyramidSolitaireModel;

public class PyramidSolitaireTextualView implements PyramidSolitaireView{
    private final PyramidSolitaireModel<?> model;
    // ... any other fields you need

    public PyramidSolitaireTextualView(PyramidSolitaireModel<?> model) {
        this.model = model;
    }

    // TODO
    @Override
    public void render() {};

    @Override
    public String toString() {
        return model.toString();
    }
}
