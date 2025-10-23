package cs3500.pyramidsolitaire.model.hw02;

import java.util.Objects;

public final class Card {
    private final CardType type;
    private final Suit suit;

    public Card(CardType cardType, Suit suit) {
        this.type = cardType;
        this.suit = suit;
    }

    public int getValue(){ return this.type.getCardValue(); }

    public CardType getType(){ return this.type; }

    public Suit getSuit(){ return this.suit; }

    @Override
    public String toString() {
        if (this.getValue() == 10) {
            return type.toString() + suit.toString();
        } else {
            return " " + type.toString() + suit.toString();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Card)) return false;
        Card that = (Card)obj;
        return this.type == that.type &&
                this.suit == that.suit;
    }

    @Override
    public int hashCode() { return Objects.hash(this.type, this.suit); }
}
