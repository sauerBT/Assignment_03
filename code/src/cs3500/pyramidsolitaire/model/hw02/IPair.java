package cs3500.pyramidsolitaire.model.hw02;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents the Pyramid Solitaire "Pair" object
 *<p>
 *     The "Pair" objects defines the position, row number, and data object to represent a "pair" in a game of Solitaire.
 *</p>
 *
 * @author Brian Sauerborn
 * @version 1.0
 * @since 1.0
 */
public interface IPair<K> {
    /**
     * This produces a Pair.
     *
     * @param position The position of the Pair in the game of Solitaire.
     * @param rowNum The row of the Pair in the game of Solitaire.
     * @param c The data object of the Pair in the game of Solitaire.
     * @return The Pair object
     * @param <K> The Pair object defined in a game of Solitaire.
     */
    static <K> IPair<K> of(Integer position, Integer rowNum, Optional<K> c) { return new Pair<>(position, rowNum, c); }

    static <K> IPair<K> empty(Integer position, Integer rowNum) { return new Pair<>(position, rowNum, Optional.empty()); }

    Integer position();

    IPair<K> position(Integer pos);

    Integer rowNum();

    IPair<K> rowNum(Integer rowNum);

    Optional<K> element();
}

class Pair<K> implements IPair<K> {
    /**
     * The position of the Pair in the game of Solitaire.
     *
     * @since 1.0
     */
    private final Integer position;

    /**
     * The row of the Pair in the game of Solitaire.
     *
     * @since 1.0
     */
    private final Integer rowNum;

    /**
     * The data object of the Pair in the game of Solitaire.
     *
     * @since 1.0
     */
    private final Optional<K> c;

    protected Pair(Integer position, Integer rowNum, Optional<K> c) {
        this.position = position;
        this.rowNum = rowNum;
        this.c = c;
    }

    @Override
    public Integer position() { return this.position; }

    @Override
    public IPair<K> position(Integer pos) { return new Pair<>(pos, this.rowNum, this.c); }

    @Override
    public Integer rowNum() { return this.rowNum; }

    @Override
    public IPair<K> rowNum(Integer rowNum) { return new Pair<>(this.position, rowNum, this.c); }

    @Override
    public Optional<K> element() { return this.c; }

    @Override
    public String toString() { return "P" + this.position + "R" + this.rowNum + (c.isPresent() ? c.get().toString() : "EMPTY"); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Pair<?>)) return false;
        Pair<?> that = (Pair<?>)obj;
        return Objects.equals(this.c, that.c) &&
                this.position.equals(that.position) &&
                this.rowNum.equals(that.rowNum);
    }

    @Override
    public int hashCode() { return Objects.hash(this.position, this.rowNum, this.c); }
}