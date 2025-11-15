package controller;

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
}