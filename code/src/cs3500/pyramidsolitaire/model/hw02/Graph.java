package cs3500.pyramidsolitaire.model.hw02;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

// Invariant: Subject Vertices cannot reference themselves, and they also cannot reference Vertices that reference themselves
public class Graph<K> {
    private final List<Vertex<K>> vertices;
    private final Optional<Vertex<K>> entryPoint;

    public Graph() {
        this.vertices = new ArrayList<>();
        this.entryPoint = Optional.empty();
    }

    private Graph(List<Vertex<K>> vertices) {
        this.vertices = vertices;
        this.entryPoint = Optional.empty();
    }

    private Graph(List<Vertex<K>> vertices, Vertex<K> entryPoint) {
        this.vertices = vertices;
        this.entryPoint = Optional.of(entryPoint);
    }

    public Graph<K> entryPoint(Vertex<K> entryPoint) { return new Graph<>(this.vertices, entryPoint); }

    /**
     * Produce a Graph with a new statement added.
     *
     * @param fromPair The given Subject data object.
     * @param toPair The given Object data object.
     * @param predicate The given statement "predicate" to define the relationship between Subject and Object.
     * @return A Graph with a new triple added.
     */
    public Graph<K> addTriple(K fromPair, K toPair, GraphPred predicate) {
        Vertex<K> tempObject = this.getIfContains(new Vertex<>(toPair)); // 1
        Vertex<K> tempSubject = this.getIfContains(new Vertex<>(fromPair)); // 2
        tempSubject.addEdge(predicate, tempObject); // 3
        return
                new Graph<>(Util.ListUtil.findIfExclude(IPred2.vertexSame(), this.vertices, new ArrayList<>(List.of(tempSubject, tempObject))))
                        .addVertex(tempObject)
                        .addVertex(tempSubject);
    }

    /**
     * Query for an existing Vertex and produce it if it exists, otherwise produce the given Vertex.
     *
     * @param v The given Vertex to query for.
     * @return The search result for the given Vertex.
     */
    private Vertex<K> getIfContains(Vertex<K> v) { return Util.ListUtil.findOne(IPred2.vertexSame(), this.vertices, v).orElse(v); }

    /**
     * Produce a Graph with the given Vertex added to the list of vertices
     *
     * @param v The given vertex to add to the Graph.
     * @return A Graph with the new Vertex added.  This is a copy, not a reference.
     * @throws IllegalArgumentException If the Vertex already exists, throw an exception.
     */
    public Graph<K> addVertex(Vertex<K> v) {
        if (Util.ListUtil.contains(IPred2.vertexSame(), this.vertices, v)) {
            throw new IllegalArgumentException("Vertex " + v.toString() + " exists for " + this.vertices.toString());
        } else {
            List<Vertex<K>> result = Util.ListUtil.copy(this.vertices);
            result.add(v);
            return new Graph<>(result);
        }
    }

    /**
     * Produce this pyramid as a list of data (elements).
     *
     * @return the list of data.
     */
    public List<K> extractData() {
        return Util.ListUtil.map(new VertexToData<>(), this.vertices);
    }

    /**
     * Produce a new copy of this graph with the given data element removed from ALL Triples.
     * NOTE: removal of a vertex from a graph is a linear time exercise since the algorithm MUST be exhaustive,
     * and therefore the Graph search is not used.
     *
     * @param vertex The data element to be removed.
     * @return The new copy of the Graph.
     */
    public Graph<K> removeElement(Vertex<K> vertex) {
        // 1. Remove all vertices that match the given vertices (this is okay because any vertex that matches the
        // given vertex would also contain edges that include the given vertex
        List<Vertex<K>> filteredLov = Util.ListUtil.findIfExclude(new ExcludeVertex<>(), this.vertices, new ArrayList<>(List.of(vertex)));
        // 2. Execute a remove edges function that passes the work to the Vertex class
        List<Vertex<K>> filteredLovWithEdgesRemoved = Util.ListUtil.foldl(new RemoveEdges<>(), filteredLov, new RemoveEdgesAcc<>(new ArrayList<>(), vertex)).lov();
        // 3. create new graph
        return new Graph<>(filteredLovWithEdgesRemoved);
    }

    public List<Vertex<K>> getVertices() { return this.vertices; }

    public Graph<K> filterVertices(Predicate<Vertex<K>> predicate) {
        return new Graph<>(Util.ListUtil.filter(predicate, this.vertices));
    }

    public Graph<K> sortVertices(Comparator<Vertex<K>> comparator) {
        this.vertices.sort(comparator);
        return new Graph<>(this.vertices); }

    /**
     * Produce a list of the data from all vertices with no edges.
     *
     * @return list of uncovered elements.
     */
    public List<K> getZeroEdgeVertices() {
        return Util.ListUtil.map(new VertexToData<>(),
                Util.ListUtil.filter(new NoEdge<>(), this.vertices)); }

    // TODO
    @Override
    public String toString() {
        return this.vertices.toString();
    }

    // TODO
    public int hashCode() { return Objects.hash(this.vertices); }

    // TODO
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Graph)) return false;
        Graph<K> that = (Graph<K>)obj;
        return this.vertices.equals(that.vertices);
    }

}

/**
 * Predicate function class that compares two vertices and produces true if they are equal
 * @param <K> The type of data contained within the vertex
 */
class ExcludeVertex<K> implements IPred2<Vertex<K>> {
    public boolean apply(Vertex<K> arg1, Vertex<K> arg2) {
        return arg1.equals(arg2);
    }
}

/**
 * Function used for comparison of Vertices to a given Vertex and removal of that vertex if the match is found.
 * @param <K> The type of data contained within the Vertex
 */
class RemoveEdges<K> implements BiFunction<Vertex<K>, RemoveEdgesAcc<K>, RemoveEdgesAcc<K>> {
    public RemoveEdgesAcc<K> apply(Vertex<K> vertex, RemoveEdgesAcc<K> acc) {
        acc.lov().add(vertex.removeEdgesWithVertex(acc.accVertex()));
        return new RemoveEdgesAcc<>(acc.lov(), acc.accVertex());
    }
}

class RemoveEdgesAcc<K> {
    List<Vertex<K>> lov;
    Vertex<K> accVertex;
    RemoveEdgesAcc(List<Vertex<K>> lov, Vertex<K> accVertex) {
        this.lov = lov;
        this.accVertex = accVertex;
    }
    public List<Vertex<K>> lov() { return this.lov; }
    public Vertex<K> accVertex() { return this.accVertex; }
}

/**
 * Function class for extracting data from a vertex
 *
 * @param <K> the data
 */
class VertexToData<K> implements Function<Vertex<K>, K> {
    public K apply(Vertex<K> vertex) { return vertex.data(); }
}

class NoEdge<K> implements Predicate<Vertex<K>> {
    public boolean test(Vertex<K> vertex) { return vertex.getEdges().isEmpty(); }
}