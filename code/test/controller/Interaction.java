package controller;

import cs3500.pyramidsolitaire.view.PyramidSolitaireView;

public interface Interaction {
    void apply(StringBuilder input, StringBuilder output);

    static Interaction prints(String... lines) {
        return (input, output) -> {
            for (String line : lines) {
                output.append(line).append('\n');
            }
        };
    }

    static Interaction inputs(String in) {
        return (input, output) -> {
            input.append(in);
        };
    }

    static Interaction transmitGameState() {
        return (input, output) -> {
            output.append("Model toString method called!\n");
        };
    }
    static Interaction transmitGameScore() {
        return (input, output) -> {
            output.append("Score: 101\n");
        };
    }

    static Interaction transmitGameOverNoWin() {
        return (input, output) -> {
            output.append("Game over. Score: 101\n");
        };
    }

    static Interaction transmitGameOverWin() {
        return (input, output) -> {
            output.append("You win!\n");
        };
    }
}