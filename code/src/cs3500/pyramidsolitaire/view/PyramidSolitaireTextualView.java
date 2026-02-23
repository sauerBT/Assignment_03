package cs3500.pyramidsolitaire.view;

import cs3500.pyramidsolitaire.model.hw02.PyramidSolitaireModel;

import java.io.IOException;

public class PyramidSolitaireTextualView implements PyramidSolitaireView{
    private final PyramidSolitaireModel<?> model;
    private final Appendable outStream;

    // TODO
    public PyramidSolitaireTextualView(PyramidSolitaireModel<?> model, Appendable outStream) {
        if (outStream != null) {
            this.model = model;
            this.outStream = outStream;
        } else {
            throw new IllegalArgumentException("Provided output stream cannot be null!");
        }
    }

    @Override
    public void renderScore() throws IOException {
        this.outStream.append(String.format("Score: %d\n", model.getScore()));
    }

    @Override
    public void renderQuit() throws IOException {
        this.outStream.append("Game Quit!\n");
        this.outStream.append("State of the game when quit:\n");
        this.render();
        this.renderScore();
    }

    @Override
    public void render() throws IOException {
        this.outStream.append(this.model.toString());
    };

    @Override
    public void askForInput() throws IOException {
        this.outStream.append("Entry not complete! Please add additional inputs!\n");
    }

    @Override
    public String toString() {
        return model.toString();
    }
}
