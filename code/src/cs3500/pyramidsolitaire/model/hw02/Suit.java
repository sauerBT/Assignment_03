package cs3500.pyramidsolitaire.model.hw02;

/**
 * A Suit is one of the following Strings:
 * - Spade
 * - Heart
 * - Diamond
 * - Club
 * Interpretation: the String represents all possible suits of a typical playing card.
 */
public enum Suit {
    Spade("♣"), Heart("♥"), Diamond("♦"), Club("♠");

    private final String value;

    /**
     * Produces a play card suit with an associated symbol
     * <p>
     *     This constructor is called automatically when the enum constants
     *     are initialized. It cannot be called directly
     * </p>
     * @param value The suit symbol of a playing card of a specific type
     */
    Suit(String value) { this.value = value; }

    @Override
    public String toString() { return this.value; }
}
