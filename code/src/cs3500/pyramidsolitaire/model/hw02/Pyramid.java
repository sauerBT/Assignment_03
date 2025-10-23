package cs3500.pyramidsolitaire.model.hw02;
import java.sql.Array;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The pyramid used to represent the "triangle" in a game of pyramid solitaire: this maintains
 * the state, generates, and enforces the rules of the triangle.
 *
 * @param <K>  the type of cards this model uses
 */
public class Pyramid<K>{
    /**
     * The current game pyramid.
     *
     * @since 1.0
     */
    private final Graph<IPair<K>> pyramid;

    /**
     * The original number of rows in the pyramid.
     *
     * @since 1.0
     */
    private final int numRows;

    // TODO -- make private? Make public interface with "of" and "empty"?
    public Pyramid(int numRows, List<K> deck) {
        this.numRows = numRows;
        this.pyramid = dealDeck(numRows, deck);
    }

    public Pyramid(int numRows) {
        this.numRows = numRows;
        this.pyramid = new Graph<>();
    }

    private Pyramid(int numRows, Graph<IPair<K>> pyramid) {
        this.numRows = numRows;
        this.pyramid = pyramid;
    }

    /**
     * Produce a full Graph representing a game Pyramid.
     * *<p>
     *     INVARIANTS:
     *     1. The given deck must have at least as many cards required to fill a full pyramid (e.g. for three rows there must be at least 6 elements in the given deck)
     *     3. The given number of rows can not be zero or negative.
     *  *</p>
     *
     * @param deck A list of elements constituting the initial deck.
     * @return The Graph representing a game pyramid.
     * @throws IllegalArgumentException if the deck is null or invalid,
     * or a full pyramid cannot be dealt with the given sizes
     */
    private static <K> Graph<IPair<K>> dealDeck(int numRows, List<K> deck) {
        if (deck == null || deck.isEmpty()) {
            throw new IllegalArgumentException("Given deck is invalid.");
        } else if (!isDeckDealable(numRows, deck.size())) {
            throw new IllegalArgumentException("Deck size is too small for the given number of rows");
        } else {
            List<IPair<K>> initAcc = new ArrayList<>(List.of(IPair.of(0, 0, Optional.of(deck.getFirst()))));
            deck.removeFirst();
            List<IPair<K>> convDeck = Util.ListUtil.foldl(new CardToPair<>(), Util.ListUtil.getFirstX(deck, Util.sumUp(numRows) - 1), initAcc);
            return  Util.ListUtil.foldl(new PairToGraph<>(), convDeck, new IPairGraphAcc<>(convDeck, new Graph<>())).g();
        }
    }

    /**
     * Determine the validity of the given deck size and row number.
     *
     * @param numRows The intended number of rows in the given pyramid.
     * @param sizeDeck The size of the given deck to be used to generate the pyramid
     * @return True if the given deck and number of rows are of valid size, otherwise false.
     */
    private static boolean isDeckDealable(int numRows, int sizeDeck) {
        return ((sizeDeck - Util.sumUp(numRows)) >= 0) &&
                (numRows > 0);
    }

    /**
     * Produce the original number of rows in this pyramid.
     *
     * @return Original number of rows.
     */
    public int getNumRows() { return this.numRows; }
    /**
     * Produce the width (number of elements) in the given row.
     *
     * @param row The given row.
     * @return The number of elements in the given row.
     * @throws IllegalArgumentException The given row is invalid.
     * @throws IllegalStateException The game has not started.
     */
    public int getRowWidth(int row) {
        if (!isRowValid(row)) {
            throw new IllegalArgumentException("Requested row is not valid.");
        } else {
            return Util.ListUtil.foldl(new CountOfRowX<>(), this.pyramid.getVertices(), new RowCountAcc(0, row)).count();
        }
    }

    /**
     * Produce true if the given row is valid, otherwise false.
     *
     * @param row The given row.
     * @return True if the given row is valid.
     */
    private boolean isRowValid(int row) {
        return 0 <= Util.ListUtil.findOne(
                new SameObj<>(),
                Util.ListUtil.map(new VertexToRow<>(), this.pyramid.getVertices()),
                row).orElse(-1);
    }

    /**
     * Produce the number of elements in this pyramid.
     *
     * @return The number of elements in this pyramid.
     */
    public int size() { return pyramid.getVertices().size(); }

    /**
     * Produce the card element at the given position and row.
     *
     * @param row The requested row.
     * @param pos The requested position.
     * @return The requested card element.
     */
    public K getCardAt(int row, int pos) {
        if (!this.isRowValid(row)) {
            throw new IllegalArgumentException("Invalid row given."); // Fail fast on a given invalid row
        } else {
            return this.getVertexAt(row, pos).data().element()
                    .orElseThrow(() -> new IllegalArgumentException("No card at the given position"));
        }
    }

    /**
     * Produce the Pair at the given row and position.
     *
     * @param row The given row.
     * @param pos The given position.
     * @return The Pair.
     */
    private Vertex<IPair<K>> getVertexAt(int row, int pos) {
        return Util.ListUtil.findOne(new SamePairLocation<>(), this.pyramid.getVertices(), new Vertex<>(IPair.of(pos, row, Optional.empty())))
                .orElseThrow(() -> new IllegalArgumentException("Card at given location not found."));
    }

    // TODO
    /**
     *
     * @param ele
     * @return
     */
    public Pyramid<K> addElement(K ele) {
        return new Pyramid<>(0, new ArrayList<>());
    }

    /**
     * Remove a single element from this pyramid given a position and row
     * INVARIANTS:
     * 1. An element with Edges is NOT removable (Edges implies that the given element is COVERED)
     * 2. It is a GUARANTEE that if an Edge IS removable, and is subsequently removed, that the Vertex has TWO Edges that
     * reference IT as a TO (or Object) UNLESS the Vertex is within rows 1 or 2 (0 or 1 in base 0). This invariant comes
     * from the fact that every Vertex in Rows  greater than 2 is a blocker for two positions in the previous Row
     *
     * @param rowNum The given row of the element to be removed.
     * @param pos The given position of the element to be removed.
     * @return A new copy of this pyramid with the given element at the given row and position removed
     * @throws IllegalArgumentException A card is not removable when covered by any other card
     */
    public Pyramid<K> removeElement(int rowNum, int pos) {
        Vertex<IPair<K>> vertex = this.getVertexAt(rowNum, pos);
        if (!isRemovable(vertex)) {
            throw new IllegalArgumentException("Card is covered, and therefore not removable.");
        } else {
            return new Pyramid<>(this.numRows, this.pyramid.removeElement(vertex));
        }
    }

    /**
     * Produce true if the given Vertex is removable, meaning that the vertex has no edges.
     *
     * @param v The given Vertex
     * @return True if the given Vertex has no edges
     */
    private boolean isRemovable(Vertex<IPair<K>> v) { return v.getEdges().isEmpty(); }

    /**
     * Produce this pyramid as a list of data (elements).
     *
     * @return the list of data.
     */
    public List<IPair<K>> extractIPair() { return this.pyramid.extractData(); }

    /**
     * Produce a list of all uncovered elements.
     *
     * @return list of uncovered elements.
     */
    public List<IPair<K>> getUncoveredCards() { return this.pyramid.getZeroEdgeVertices(); }

    /**
     * Produce true if the given row is empty, otherwise false.
     *
     * @param rowNum The given row number.
     * @return True if the row is empty.
     */
//    public boolean isRowEmpty(int rowNum) { return false; } // STUB
    public boolean isRowEmpty(int rowNum) {
        return this.pyramid.filterVertices(new FindPairsInRow<>(rowNum)).getVertices().isEmpty();
    }

    // TODO
    /**
     * Produce the list of cards covering the given card at the given vertex.
     * @param v The given vertex.
     * @return List of Cards.
     * @throws IllegalArgumentException The given Vertex is not within this pyramid.
     */
    public List<K> coveringCards(Vertex<IPair<K>> v) { return new ArrayList<>(); }

    // TODO
    /**
     * Produce the number of cards covering the given card at the given vertex.
     * @param v The given vertex.
     * @return The count.
     * @throws IllegalArgumentException The given Vertex is not within this pyramid.
     */
    public int coveringCardsCount(Vertex<IPair<K>> v) { return 0; }

    @Override
    public String toString() {
        return toStringHelper(0, new StringBuilder());
    }

    private String toStringHelper(int rowWl, StringBuilder acc) {
        if (rowWl == this.getNumRows()) {
            return acc.toString();
        } else {
            if (rowWl == (this.getNumRows() - 1)) {
                return toStringHelper(rowWl + 1, acc.append(this.rowStringBuilder(rowWl)));
            } else {
                return toStringHelper(rowWl + 1, acc.append(this.rowStringBuilder(rowWl)).append("\n"));
            }
        }
    }

    private String rowStringBuilder(int rowNum) {
         Graph<IPair<K>> filteredSortedGraph = this.pyramid.filterVertices(new FindPairsInRow<>(rowNum))
                 .sortVertices(new SortPairByPosition<>());
         List<IPair<K>> filteredSortedFilledList = Util.GameUtil.fillEmptyPositions(rowNum, filteredSortedGraph.extractData());
         return this.formatRowToString(filteredSortedFilledList);
    }

    /**
     * Produce a string representation of the given row of elements.
     *
     * @param loe The given row of elements
     * @return String representation of a row.
     */
    private String formatRowToString(List<IPair<K>> loe) {
        if (loe.isEmpty()) {
            return "";
        } else {
            if (this.isRowEmpty(loe.getFirst().rowNum())) {
                return "";
            } else {
                int baseZeroNumRows = this.numRows - 1;
                return Util.ListUtil.foldl(
                        new RowToString<>(),
                        loe,
                        new RowToString.RowToStringAcc(baseZeroNumRows, 0, this.padRowWithSpaces(baseZeroNumRows - loe.getFirst().rowNum()))).acc();
            }
        }
    }

    /**
     * Produce the whitespace padding for a given row number.
     *
     * @param spacesToPad The given row number.
     * @return The whitespace padding as a string.
     */
    private String padRowWithSpaces(int spacesToPad) {
        return this.padRowWithSpacesHelper(spacesToPad, 0, "");
    }

    private String padRowWithSpacesHelper(int spacesToPad, int spacesAcc, String padAcc) {
        if (spacesAcc >= spacesToPad) {
            return padAcc;
        } else {
            return this.padRowWithSpacesHelper(spacesToPad, spacesAcc + 1, padAcc + "  ");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Pyramid<?>)) return false;
        Pyramid<?> that = (Pyramid<?>)obj;
        return this.pyramid.equals(that.pyramid) &&
                this.numRows == that.numRows;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pyramid, numRows);
    }
}

/**
 * A function class used to wrap elements in IPair
 *
 * @param <K>  the type of cards this function class uses
 */
class CardToPair<K> implements BiFunction<K, List<IPair<K>>, List<IPair<K>>> {
    public List<IPair<K>> apply(K c, List<IPair<K>> loi) {
        IPair<K> prev = loi.getLast();
        int prevPos = prev.position();
        int prevRowNum = prev.rowNum();
        if (prevPos == prevRowNum) {
            loi.addLast(IPair.of(0, prevRowNum + 1, Optional.of(c)));
        } else {
            loi.addLast(IPair.of(prevPos + 1, prevRowNum, Optional.of(c)));
        }
        return loi;
    }
}

/**
 * A bifunction class used to "fold" IPairs into vertices within a Graph.
 *
 * @param <K>  the type of cards this bifunction class uses
 */
class PairToGraph<K> implements BiFunction<IPair<K>, IPairGraphAcc<IPair<K>>, IPairGraphAcc<IPair<K>>> {
    public IPairGraphAcc<IPair<K>> apply(IPair<K> p, IPairGraphAcc<IPair<K>> acc) {
        Optional<IPair<K>> o1 = Util.ListUtil.findOne(new LeftNode<>(), acc.loi(), p);
        Optional<IPair<K>> o2 = Util.ListUtil.findOne(new RightNode<>(), acc.loi(), p);
        Graph<IPair<K>> result1 = o1.map(pair -> acc.g().addTriple(p, pair, GraphPred.Child))
                .orElse(acc.g());

        Graph<IPair<K>> result2 = o2.map(pair -> result1.addTriple(p, pair, GraphPred.Child))
                .orElse(result1);

//        Graph result = acc.g();
//        if (o1.isPresent()) { result = result.addTriple(p, o1.get(), GraphPred.Child); } // Last Step -- Generalize Graph / Vertices / Edge
//        if (o2.isPresent()) { result = result.addTriple(p, o2.get(), GraphPred.Child); } // Last Step -- Generalize Graph / Vertices / Edge
        return new IPairGraphAcc<>(acc.loi(), result2);
    }
}

class LeftNode<K> implements IPred2<IPair<K>> {
    public boolean apply(IPair<K> arg1, IPair<K> arg2) {
        return ((arg2.position() == arg1.position()) && (arg2.rowNum() == (arg1.rowNum() - 1)));
    }
}

class RightNode<K> implements IPred2<IPair<K>> {
    public boolean apply(IPair<K> arg1, IPair<K> arg2) {
        return ((arg2.position() == arg1.position() - 1) && (arg2.rowNum() == (arg1.rowNum() - 1)));
    }
}

class SameObj<K> implements IPred2<K> {
    public boolean apply(K arg1, K arg2) {
        return arg1.equals(arg2);
    }
}

class SamePairLocation<K> implements IPred2<Vertex<IPair<K>>> {
    public boolean apply (Vertex<IPair<K>> arg1, Vertex<IPair<K>> arg2) {
        return arg1.data().rowNum().equals(arg2.data().rowNum()) && arg1.data().position().equals(arg2.data().position());
    }
}

// TODO -- Remove cast...
class VertexToRow<K, Integer> implements Function<Vertex<IPair<K>>, Integer> {
    public Integer apply(Vertex<IPair<K>> vertex) {
        return (Integer) vertex.data().rowNum();
    }
}

/**
 * Increments the counter if a particular row is found.
 *
 * @param <K>
 */
class CountOfRowX<K> implements BiFunction<Vertex<IPair<K>>, RowCountAcc, RowCountAcc> {
    public RowCountAcc apply(Vertex<IPair<K>> vertex, RowCountAcc acc) {
        if (acc.row().equals(vertex.data().rowNum())) {
            return new RowCountAcc(acc.count() + 1, acc.row());
        } else {
            return new RowCountAcc(acc.count(), acc.row());
        }
    }
}

class FindPairsInRow<K> implements Predicate<Vertex<IPair<K>>> {
    int rowNum;
    FindPairsInRow(int rowNum) { this.rowNum = rowNum; }
    public boolean test(Vertex<IPair<K>> k) { return k.data().rowNum() == this.rowNum; }
}

class SortPairByPosition<K> implements Comparator<Vertex<IPair<K>>> {
    public int compare(Vertex<IPair<K>> o1, Vertex<IPair<K>> o2) {
        if (o1.data().position() <= o2.data().position()) {
            return -1;
        } else if (o1.data().position() > o2.data().position()) {
            return 1;
        } else {
            return 0;
        }
    }
}

class RowToString<K> implements BiFunction<IPair<K>, RowToString.RowToStringAcc, RowToString.RowToStringAcc> {
    record RowToStringAcc(Integer maxRows, Integer positionAcc, String acc) {};

    public RowToStringAcc apply(IPair<K> k, RowToStringAcc s) {
        if (s.positionAcc.equals(0)) { // <-- first position
            if (!k.position().equals(s.positionAcc) || k.element().isEmpty()) { // <-- No element at this position
                return new RowToStringAcc(s.maxRows(), s.positionAcc + 1, s.acc + "   ");
            } else {
                return new RowToStringAcc(s.maxRows(), s.positionAcc + 1, s.acc + k.element().get().toString().stripLeading() + " ");
            }
        } else { // <-- Not First Position
            if (!k.position().equals(s.positionAcc) || k.element().isEmpty()) { // <-- No element at this position
                return new RowToStringAcc(s.maxRows(), s.positionAcc + 1, s.acc + "    ");
            } else {
                return new RowToStringAcc(s.maxRows(), s.positionAcc + 1, s.acc + k.element().get().toString() + " ");
            }
        }
    }
}

record RowCountAcc(Integer count, Integer row) {
}

record IPairGraphAcc<K>(List<K> loi, Graph<K> g) {
}