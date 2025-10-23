package cs3500.pyramidsolitaire.model.hw02;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;
// TODO Future enhancement: Make pair generic
/**
 * The Vertex of a Graph data structure that is used to represent graphical relationships.
 *<p>
 *     This collection is used to represent graphical relationships and enforces rules to maintain the integrity of
 *     the Graph data structure.  This class uses mutability sparingly in order to easily maintain self references and
 *     mutual references allowed by a graph data structure without exposing the mutability to an external client.
 *</p>
 *
 * @author Brian Sauerborn
 * @version 1.0
 * @since 1.0
 */
public class Vertex<K> {
    /**
     * The collection of "Edges" (Predicates) that define relationships between this Vertex and other Vertices.
     * NOTE: This list is MUTABLE.
     * @since 1.0
     */
    private List<Edge<K>> edges;

    /**
     * The data object that defines this Vertex.
     *
     * @since 1.0
     */
    private final K pair;

    public Vertex(K pair) {
        this.pair = pair;
        this.edges = new ArrayList<>();
    }
    
    private Vertex(List<Edge<K>> edges, K pair) {
        this.edges = edges;
        this.pair = pair;
    }

    // TODO - Testing and add why mutability of the object in this case is safe
    /**
     * Produce a new Vertex with a new Triple added to the edges List
     *<p>
     *      SAFE MUTATION: The edges collection of this Vertex is mutated, and therefore this Vertex itself is mutated. This
     *      is safe because ...
     * </p>
     * @param predicate The relationship or edge of the new triple
     * @param to The Object of the Triple
     * @throws IllegalArgumentException Error when the given predicate and Object create an Edge that already exists
     */
    public void addEdge(GraphPred predicate, Vertex<K> to) {
        Edge<K> newEdge = new Edge<>(predicate, this, to);
        if (this.edges.contains(newEdge)) {  // Confirm the edge to be added does not already exist
            throw new IllegalArgumentException(
                    "Identical Edge: " + newEdge.toString() + " already exists for " + this.toString() + ". Edges: " + this.edges.toString()
            );
        } else {
            this.edges.add(newEdge);
        }
    }

    public List<Edge<K>> getEdges() { return this.edges; }

    public K data() { return this.pair; }
    
    /**
     * Produce a true|false based on whether the Vertex has existing edges
     *
     * @return True if there are existing edges, otherwise false
     */
    public boolean isEmpty() { return this.edges.isEmpty(); }

    /**
     * Produce true if the given Vertex is the same Subject as this Vertex. The "sameness" used here does not include
     * the list of Edges
     *
     * @param obj The given Vertex to compare this Vertex to
     * @return True if this Vertex has the same data as the given Vertex.
     */
    public boolean sameSubject(Vertex<K> obj) { return this.pair.equals(obj.pair); }

    /**
     * Produce a shallow copy of this Vertex.
     *
     * @return A copy of this instance.
     */
    public Vertex<K> copy() { return new Vertex<>(Util.ListUtil.copy(this.edges), this.pair); }

    /**
     * Remove all edges that contain the given Vertex in their "To" or "From" element.
     *
     * @param vertex The given vertex to compare with each edge
     * @return The vertex with a filtered edges field.
     */
    public Vertex<K> removeEdgesWithVertex(Vertex<K> vertex) {
        return new Vertex<>(
                Util.ListUtil.foldl(new RemoveEdgesForVertex<>(), this.getEdges(), new RemoveEdgesForVertexAcc<>(vertex, new ArrayList<>())).accEdges(),
                this.data());
    }

    // TODO
    @Override
    public String toString() {
        return this.pair.toString();
    }

    // TODO -- Deep hashCode?
    public int hashCode() {
        return Objects.hash(pair);
    }

    // TODO -- Deep equals?
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vertex)) return false;
        Vertex<K> that = (Vertex<K>)obj;
        return this.pair.equals(that.pair);
    }
}

class RemoveEdgesForVertex<K> implements BiFunction<Edge<K>, RemoveEdgesForVertexAcc<K>, RemoveEdgesForVertexAcc<K>> {
    public RemoveEdgesForVertexAcc<K> apply(Edge<K> edge, RemoveEdgesForVertexAcc<K> acc) {
        if (edge.containsVertex(acc.vertex())) {
            return acc;
        } else {
            acc.accEdges().add(edge);
            return new RemoveEdgesForVertexAcc<>(acc.vertex(), acc.accEdges());
        }
    }
}

class RemoveEdgesForVertexAcc<K> {
    Vertex<K> vertex;
    List<Edge<K>> accEdges;
    RemoveEdgesForVertexAcc(Vertex<K> vertex, List<Edge<K>> accEdges) {
        this.vertex = vertex;
        this.accEdges = accEdges;
    }
    public Vertex<K> vertex() { return this.vertex; }
    public List<Edge<K>> accEdges() { return this.accEdges; }
}
