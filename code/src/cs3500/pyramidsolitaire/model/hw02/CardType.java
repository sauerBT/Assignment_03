package cs3500.pyramidsolitaire.model.hw02;

/**
 * A CardType is one of the following String, Integer pairs:
 * - Ace, 1
 * - Two, 2
 * - Three, 3
 * - Four, 4
 * - Five, 5
 * - Six, 6
 * - Seven, 7
 * - Eight, 8
 * - Nine, 9
 * - Ten, 10
 * - Jack, 11
 * - Queen, 12
 * - King, 13
 * Interpretation: the String and Integer pairs represent all the possible face types and associated numerical values
 * that a typical playing card may assume
 */
public enum CardType {
    Two(2), Three(3), Four(4), Five(5),
    Six(6), Seven(7), Eight(8), Nine(9),
    Ten(10), Jack(11), Queen(12), King(13),
    Ace(1);

    private final int value;

    /**
     * Produces a play card type with an associated numerical value
     * <p>
     *     This constructor is called automatically when the enum constants
     *     are initialized. It cannot be called directly
     * </p>
     * @param value The numerical value of a playing card of a specific type
     */
    CardType(int value) { this.value = value; }

    public int getCardValue() { return this.value; }

    @Override
    public String toString() {
        if (this.value == 1) {
            return "A";
        } else if (this.value == 11) {
            return "J";
        } else if (this.value == 12) {
            return "Q";
        } else if (this.value == 13) {
            return "K";
        } else {
            return String.format("%d", this.value);
        }
    }
}
