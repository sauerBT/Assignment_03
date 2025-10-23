package model;
import org.junit.*;
import static org.junit.Assert.*;
import cs3500.pyramidsolitaire.model.hw02.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GraphTest {
    // Example Cards
    Card C01;
    Card C02;
    Card C03;
    Card C04;
    // Example Pairs
    IPair<Card> P00;
    IPair<Card> P01;
    IPair<Card> P02;
    IPair<Card> P03;
    IPair<Card> P04;
    IPair<Card> P05;
    IPair<Card> P06;
    // Example Edges
    Edge<IPair<Card>> E01;
    Edge<IPair<Card>> E02;
    Edge<IPair<Card>> E03;
    Edge<IPair<Card>> E04;
    Edge<IPair<Card>> E05;
    Edge<IPair<Card>> E06;
    // Example Vertices
    Vertex<IPair<Card>> V00;
    Vertex<IPair<Card>> V001;
    Vertex<IPair<Card>> V01;
    Vertex<IPair<Card>> V02;
    Vertex<IPair<Card>> V03;
    Vertex<IPair<Card>> V04;
    Vertex<IPair<Card>> V05;
    Vertex<IPair<Card>> V06;
    // Example Graphs
    Graph<IPair<Card>> G00;
    Graph<IPair<Card>> G01;
    Graph<IPair<Card>> G02;
    Graph<IPair<Card>> G04;
    Graph<IPair<Card>> G03;
    Graph<IPair<Card>> G05;
    Graph<IPair<Card>> G06;


    @Before
    public void setupTestFixture() {
        // Initialize Example Cards
        C01 = new Card(CardType.King, Suit.Heart);
        C02 = new Card(CardType.Queen, Suit.Spade);
        C03 = new Card(CardType.Jack, Suit.Diamond);
        C04 = new Card(CardType.Ace, Suit.Club);

        // Initialize Example Pairs
        P01 = IPair.of(0, 0, Optional.of(C01));
        P02 = IPair.of(1, 1, Optional.of(C02));
        P03 = IPair.of(2, 1, Optional.of(C03));
        P04 = IPair.of(3, 2, Optional.of(C04));
        P05 = IPair.of(4, 2, Optional.of(new Card(CardType.Seven, Suit.Heart)));
        P06 = IPair.of(5, 2, Optional.of(new Card(CardType.Six, Suit.Club)));

        // Initialize Example Vertices
        // Empty
        V00 = new Vertex<>(P00);
        // Single Vertex
        V001 = new Vertex<>(P01);
        // Row 0
        V01 = new Vertex<>(P01);
        // Row 1
        V02 = new Vertex<>(P02);
        V03 = new Vertex<>(P03);
        // Row 2
        V04 = new Vertex<>(P04);
        V05 = new Vertex<>(P05);
        V06 = new Vertex<>(P06);

        // Initialize Example Edges
        // Row 0 --> Row 1
        V01.addEdge(GraphPred.Child, V02);
        E01 = V01.getEdges().getLast();
        V01.addEdge(GraphPred.Child, V03);
        E02 = V01.getEdges().getLast();
        // Row 1 --> Row 2
        V02.addEdge(GraphPred.Child, V04);
        E03 = V02.getEdges().getLast();
        V02.addEdge(GraphPred.Child, V05);
        E04 = V02.getEdges().getLast();
        V03.addEdge(GraphPred.Child, V05);
        E05 = V03.getEdges().getLast();
        V03.addEdge(GraphPred.Child, V06);
        E06 = V03.getEdges().getLast();

        // Initialize Example Graphs
        G00 = new Graph<>();
        G01 = G00.addTriple(P01, P02, GraphPred.Child);
        G02 = G01.addTriple(P01, P03, GraphPred.Child);
        G03 = G02.addTriple(P02, P04, GraphPred.Child);
        G04 = G03.addTriple(P02, P05, GraphPred.Child);
        G05 = G04.addTriple(P03, P05, GraphPred.Child);
        G06 = G05.addTriple(P03, P06, GraphPred.Child);

    }

    @Test
    public void hashCodeTest() {
        assertNotEquals(V00.hashCode(), new Vertex<>(P01).hashCode());
        assertEquals(V01.hashCode(), new Vertex<>(P01).hashCode());
        assertEquals(V01.hashCode(), V01.hashCode());
        assertNotEquals(V01.hashCode(), new Vertex<>(P02).hashCode());
    }

    @Test
    public void vertexEqualsTest() {
        assertNotEquals(V00, new Vertex<>(P01));
        assertEquals(V01, new Vertex<>(P01));
        assertEquals(V01, V01);
        assertNotEquals(V01, new Vertex<>(P02));
    }

    @Test
    public void edgeConstructor() {
        assertTrue(V01.getEdges().contains(E01));
        assertTrue(V01.getEdges().contains(E02));
        assertTrue(V02.getEdges().contains(E03));
        assertTrue(V02.getEdges().contains(E04));
        assertTrue(V03.getEdges().contains(E05));
        assertTrue(V03.getEdges().contains(E06));
        // Test for invalid constructor addition
    }

    @Test(expected = IllegalArgumentException.class)
    public void edgeConstructorDisallowsDuplicateEdge() { V01.addEdge(GraphPred.Child, V03); }

    @Test
    public void edgeHashCodeTest() {
        assertEquals(E01.hashCode(), Objects.hash(GraphPred.Child, V01, V02));
        assertNotEquals(E01.hashCode(), Objects.hash(GraphPred.Adjacent, V01, V02));
        assertNotEquals(E01.hashCode(), Objects.hash(GraphPred.Child, V02, V02));
        assertNotEquals(E01.hashCode(), Objects.hash(GraphPred.Child, V01, V01));
    }

//    @Test
//    public void edgeEquals() {
//        assertEquals(E01, new Vertex(P01).addEdge(GraphPred.Child, V02).getEdges());
//        assertNotEquals(E01, new Edge(GraphPred.Adjacent, V01, V02));
//        assertNotEquals(E01, new Edge(GraphPred.Child, V02, V02));
//        assertNotEquals(E01, new Edge(GraphPred.Child, V01, V01));
//    }


    @Test
    public void graphAddTripleTest() {
        // Test for same Vertex, different edge
        assertFalse(Util.ListUtil.containsDuplicatesOf(IPred2.vertexSame(), G00.getVertices(), new Vertex<>(P01))); // Empty test
        assertFalse(Util.ListUtil.containsDuplicatesOf(IPred2.vertexSame(), G02.getVertices(), new Vertex<>(P01)));
        assertFalse(Util.ListUtil.containsDuplicatesOf(IPred2.vertexSame(), G04.getVertices(), new Vertex<>(P02)));
        assertFalse(Util.ListUtil.containsDuplicatesOf(IPred2.vertexSame(), G04.getVertices(), new Vertex<>(P01)));
        assertFalse(Util.ListUtil.containsDuplicatesOf(IPred2.vertexSame(), G06.getVertices(), new Vertex<>(P03)));
        assertFalse(Util.ListUtil.containsDuplicatesOf(IPred2.vertexSame(), G06.getVertices(), new Vertex<>(P02)));
        assertFalse(Util.ListUtil.containsDuplicatesOf(IPred2.vertexSame(), G06.getVertices(), new Vertex<>(P01)));
        // Test for new Vertex
        assertEquals(G00.addTriple(P01, P02, GraphPred.Child), G01); // Empty addition
        assertFalse(Util.ListUtil.containsDuplicatesOf(IPred2.vertexSame(),
                G01.addTriple(P01, P04, GraphPred.Child).getVertices(), new Vertex<>(P01))); // Single Addition
    }

    @Test
    public void graphAddVertex() {
        assertEquals(new Graph<IPair<Card>>().addVertex(V01).getVertices(), new ArrayList<>(List.of(V01)));
        Graph<IPair<Card>> exp01 = new Graph<IPair<Card>>().addVertex(V01);
        assertSame(exp01.getVertices().getFirst().getEdges().getFirst(), E01);
        assertEquals(new Graph<IPair<Card>>().addVertex(V01).addVertex(V02).addVertex(V03).getVertices(), new ArrayList<>(List.of(V01, V02, V03)));
        Graph<IPair<Card>> exp02 = new Graph<IPair<Card>>().addVertex(V01).addVertex(V02).addVertex(V03);
        assertSame(E04, exp02.getVertices().get(1).getEdges().getLast());
        assertSame(E03, exp02.getVertices().get(1).getEdges().getFirst());
        assertSame(E05, exp02.getVertices().get(2).getEdges().getFirst());
        assertSame(E06, exp02.getVertices().get(2).getEdges().getLast());
    }

    @Test(expected = IllegalArgumentException.class)
    public void graphAddVertexDuplicate() {
        new Graph<IPair<Card>>().addVertex(V01).addVertex(V01);
        new Graph<IPair<Card>>().addVertex(V01).addVertex(V02).addVertex(V03).addVertex(V03);
        new Graph<IPair<Card>>().addVertex(V01).addVertex(V02).addVertex(V02).addVertex(V03);
    }

    @Test(expected = IllegalArgumentException.class)
    public void graphConstructorDisallowsDuplicateTriples() {
        // Test for same Vertex Edge Vertex combination
        G02.addTriple(P01, P02, GraphPred.Child);
    }

    @Test
    public void graphHashCodeTest() {
        assertEquals(new Graph<>().hashCode(), G00.hashCode()); // Empty
        assertNotEquals(new Graph<>().hashCode(), G01.hashCode()); // Empty
        assertEquals(G01.hashCode(),
                new Graph<>().addTriple(P01, P02, GraphPred.Child).hashCode()); // Single
        assertNotEquals(G01.hashCode(),
                new Graph<>().addTriple(P01, P03, GraphPred.Child).hashCode()); // Single
        assertEquals(G06.hashCode(),
                new Graph<>()
                        .addTriple(P01, P02, GraphPred.Child)
                        .addTriple(P01, P03, GraphPred.Child)
                        .addTriple(P02, P04, GraphPred.Child)
                        .addTriple(P02, P05, GraphPred.Child)
                        .addTriple(P03, P05, GraphPred.Child)
                        .addTriple(P03, P06, GraphPred.Child).hashCode()
        ); // Multi
        assertNotEquals(G06,
                new Graph<>()
                        .addTriple(P01, P02, GraphPred.Child)
                        .addTriple(P01, P03, GraphPred.Child)
                        .addTriple(P02, P04, GraphPred.Child)
                        .addTriple(P02, P05, GraphPred.Child)
                        .addTriple(P03, P05, GraphPred.Child).hashCode()
        ); // Multi
        assertNotEquals(G06,
                new Graph<>()
                        .addTriple(P01, P02, GraphPred.Child)
                        .addTriple(P01, P03, GraphPred.Child)
                        .addTriple(P02, P05, GraphPred.Child)
                        .addTriple(P03, P05, GraphPred.Child)
                        .addTriple(P03, P06, GraphPred.Child).hashCode()
        ); // Multi
    }

    @Test
    public void graphEqualsTest() {
        assertEquals(new Graph<>(), G00); // Empty
        assertNotEquals(new Graph<>(), G01); // Empty
        assertEquals(G01, new Graph<>().addTriple(P01, P02, GraphPred.Child)); // Single
        assertNotEquals(G01, new Graph<>().addTriple(P01, P03, GraphPred.Child)); // Single
        assertEquals(G06,
                new Graph<>()
                        .addTriple(P01, P02, GraphPred.Child)
                        .addTriple(P01, P03, GraphPred.Child)
                        .addTriple(P02, P04, GraphPred.Child)
                        .addTriple(P02, P05, GraphPred.Child)
                        .addTriple(P03, P05, GraphPred.Child)
                        .addTriple(P03, P06, GraphPred.Child)
        ); // Multi
        assertNotEquals(G06,
                new Graph<>()
                        .addTriple(P01, P02, GraphPred.Child)
                        .addTriple(P01, P03, GraphPred.Child)
                        .addTriple(P02, P04, GraphPred.Child)
                        .addTriple(P02, P05, GraphPred.Child)
                        .addTriple(P03, P05, GraphPred.Child)
        ); // Multi
        assertNotEquals(G06,
                new Graph<>()
                        .addTriple(P01, P02, GraphPred.Child)
                        .addTriple(P01, P03, GraphPred.Child)
                        .addTriple(P02, P05, GraphPred.Child)
                        .addTriple(P03, P05, GraphPred.Child)
                        .addTriple(P03, P06, GraphPred.Child)
        ); // Multi
    }
}
