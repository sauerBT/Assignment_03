package model;

import cs3500.pyramidsolitaire.model.hw02.*;
import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class UtilTest {
    // Example decks
    List<Card> D00;
    List<Card> D01;
    List<Card> D02;
    List<Card> D03;
    List<Card> D04;
    // Example Cards
    Card C01;
    Card C02;
    Card C03;
    Card C04;
    // Example Pairs
    IPair<Card> P01;
    IPair<Card> P02;
    IPair<Card> P03;
    IPair<Card> P04;
    IPair<Card> P05;
    IPair<Card> P06;
    // Example Draw Piles
    List<Card> DP00;
    List<Card> DP01M;
    List<Card> DP01NM;
    List<Card> DP03M;
    List<Card> DP03NM;

    // Example Pyramids as Lists
    List<Card> PAL00;
    List<Card> PAL01M;
    List<Card> PAL01NM;
    List<Card> PAL03M;
    List<Card> PAL04M;
    List<Card> PAL05M;
    List<Card> PAL03NM;

    // Example Edges
    Edge<IPair<Card>> E01;
    Edge<IPair<Card>> E02;
    Edge<IPair<Card>> E03;
    Edge<IPair<Card>> E04;
    Edge<IPair<Card>> E05;
    Edge<IPair<Card>> E06;
    // Example Vertices
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
    Graph<IPair<Card>> G03;
    Graph<IPair<Card>> G04;
    Graph<IPair<Card>> G05;
    Graph<IPair<Card>> G06;

    @Before
    public void setupTestFixture() {
        // Produce example decks
        D00 = new ArrayList<>();
        D01 = new ArrayList<>();
        D01.add(new Card(CardType.Queen, Suit.Heart));
        D02 = new ArrayList<>();
        D02.add(new Card(CardType.Ten, Suit.Spade));
        D02.add(new Card(CardType.Seven, Suit.Club));
        D03 = new ArrayList<>();
        D03.add(new Card(CardType.King, Suit.Heart));
        D03.add(new Card(CardType.Jack, Suit.Diamond));
        D03.add(new Card(CardType.Five, Suit.Club));
        D04 = new ArrayList<>();
        D04.addAll(D01);
        D04.addAll(D02);
        D04.addAll(D03);

        // Example Draw Pile
        DP00 = new ArrayList<>();
        DP01M = new ArrayList<>(
                List.of(
                        new Card(CardType.King, Suit.Club)));
        DP01NM = new ArrayList<>(
                List.of(
                        new Card(CardType.Ace, Suit.Club)));
        DP03M = new ArrayList<>(
                List.of(
                        new Card(CardType.Ace, Suit.Club),
                        new Card(CardType.Seven, Suit.Club),
                        new Card(CardType.Queen, Suit.Club)));
        DP03NM = new ArrayList<>(
                List.of(
                        new Card(CardType.Two, Suit.Club),
                        new Card(CardType.Seven, Suit.Club),
                        new Card(CardType.Queen, Suit.Club)));

        // Example Pyramid as List
        PAL00 = new ArrayList<>();
        PAL01M = new ArrayList<>(
                List.of(
                        new Card(CardType.King, Suit.Club)));
        PAL01NM = new ArrayList<>(
                List.of(
                        new Card(CardType.Two, Suit.Club)));
        PAL03M = new ArrayList<>(
                List.of(
                        new Card(CardType.Ace, Suit.Club),
                        new Card(CardType.Seven, Suit.Club),
                        new Card(CardType.Queen, Suit.Club)));
        PAL04M = new ArrayList<>(
                List.of(
                        new Card(CardType.Ace, Suit.Club),
                        new Card(CardType.King, Suit.Club),
                        new Card(CardType.Queen, Suit.Club)));
        PAL05M = new ArrayList<>(
                List.of(
                        new Card(CardType.Two, Suit.Club),
                        new Card(CardType.King, Suit.Club),
                        new Card(CardType.Queen, Suit.Club)));
        PAL03NM = new ArrayList<>(
                List.of(
                    new Card(CardType.Two, Suit.Club),
                    new Card(CardType.Seven, Suit.Club),
                    new Card(CardType.Queen, Suit.Club)));

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
    }

    @Test
    public void utilGetFirstX() {
        assertEquals(new ArrayList<>(), Util.ListUtil.getFirstX(this.D04, 0));
        assertEquals(new ArrayList<>(List.of(new Card(CardType.Queen, Suit.Heart))), Util.ListUtil.getFirstX(this.D04, 1));
        assertEquals(new ArrayList<>(List.of(
                new Card(CardType.Queen, Suit.Heart),
                new Card(CardType.Ten, Suit.Spade),
                new Card(CardType.Seven, Suit.Club)
        )), Util.ListUtil.getFirstX(this.D04, 3));
    }

    @Test
    public void utilRemoveFirstX() {
        assertEquals(this.D04, Util.ListUtil.removeFirstX(this.D04, 0));
        List<Card> expected02 = Util.ListUtil.clone(this.D04);
        expected02.removeFirst();
        assertEquals(expected02, Util.ListUtil.removeFirstX(this.D04, 1));
        List<Card> expected03 = Util.ListUtil.clone(this.D04);
        expected03.removeFirst();
        expected03.removeFirst();
        expected03.removeFirst();
        assertEquals(expected03, Util.ListUtil.removeFirstX(this.D04, 3));
    }

    @Test
    public void utilContainsDuplicates() {
        IPred2<Card> pred2 = IPred2.equalsPred();
        assertFalse(Util.ListUtil.containsDuplicatesOf(pred2, D01, new Card(CardType.Queen, Suit.Heart)));
        D01.add(new Card(CardType.Queen, Suit.Heart));
        assertTrue(Util.ListUtil.containsDuplicatesOf(pred2, D01, new Card(CardType.Queen, Suit.Heart)));
        assertFalse(Util.ListUtil.containsDuplicatesOf(pred2, D03, new Card(CardType.Five, Suit.Club)));
        D03.add(new Card(CardType.Five, Suit.Club));
        assertTrue(Util.ListUtil.containsDuplicatesOf(pred2, D03, new Card(CardType.Five, Suit.Club)));
        assertFalse(Util.ListUtil.containsDuplicatesOf(pred2, D03, new Card(CardType.Jack, Suit.Diamond)));
        D03.add(new Card(CardType.Jack, Suit.Diamond));
        assertTrue(Util.ListUtil.containsDuplicatesOf(pred2, D03, new Card(CardType.Jack, Suit.Diamond)));
    }

    @Test
    public void utilCount() {
        IPred2<Card> pred2 = IPred2.equalsPred();
        assertEquals(1, Util.ListUtil.count(pred2, D01, new Card(CardType.Queen, Suit.Heart)));
        D01.add(new Card(CardType.Queen, Suit.Heart));
        assertEquals(2, Util.ListUtil.count(pred2, D01, new Card(CardType.Queen, Suit.Heart)));
        assertEquals(1, Util.ListUtil.count(pred2, D03, new Card(CardType.Five, Suit.Club)));
        D03.add(new Card(CardType.Five, Suit.Club));
        assertEquals(2, Util.ListUtil.count(pred2, D03, new Card(CardType.Five, Suit.Club)));
        assertEquals(1, Util.ListUtil.count(pred2, D03, new Card(CardType.Jack, Suit.Diamond)));
        D03.add(new Card(CardType.Jack, Suit.Diamond));
        assertEquals(2, Util.ListUtil.count(pred2, D03, new Card(CardType.Jack, Suit.Diamond)));
        assertEquals(0, Util.ListUtil.count(pred2, D03, new Card(CardType.Two, Suit.Diamond)));
    }

    @Test
    public void utilContains() {
        assertTrue(Util.ListUtil.contains(IPred2.vertexSame(), new ArrayList<>(List.of(V01)), V01)); // Empty test - vertexSame
        assertFalse(Util.ListUtil.contains(IPred2.vertexSame(), new ArrayList<>(), V01)); // Empty test - vertexSame

        assertFalse(Util.ListUtil.contains(IPred2.VertexDiff(), new ArrayList<>(), V01)); // Empty test - vertexDiff

        assertFalse(Util.ListUtil.contains(IPred2.VertexDiff(), new ArrayList<>(List.of(V01)), V01)); // Single - Equal test
        assertTrue(Util.ListUtil.contains(IPred2.VertexDiff(), new ArrayList<>(List.of(V01)), V02)); // Single - Non-Equal test

        assertTrue(Util.ListUtil.contains(IPred2.VertexDiff(), new ArrayList<>(List.of(V01, V02, V03)), V06)); // Multi - Non-Equal test
        assertTrue(Util.ListUtil.contains(IPred2.vertexSame(), new ArrayList<>(List.of(V01, V02, V03)), V01)); // Multi - Non-Equal test
        assertTrue(Util.ListUtil.contains(IPred2.vertexSame(), new ArrayList<>(List.of(V01, V02, V03)), V03)); // Multi - Non-Equal test
    }

    @Test
    public void utilCopyList() {
        List<Integer> LOI01 = new ArrayList<>(List.of());
        List<Integer> LOI02 = new ArrayList<>(List.of(0));
        List<Integer> LOI03 = new ArrayList<>(List.of(0,1,2));
        assertEquals(LOI01, Util.ListUtil.copy(LOI01));
        assertEquals(LOI02, Util.ListUtil.copy(LOI02));
        assertEquals(LOI03, Util.ListUtil.copy(LOI03));
    }

    @Test
    public void utilFindTest() {
        assertEquals(Optional.empty(), Util.ListUtil.findOne(IPred2.vertexSame(), new ArrayList<>(), V01)); // Empty
        assertEquals(Optional.of(V01), Util.ListUtil.findOne(IPred2.vertexSame(), new ArrayList<>(List.of(V01)), V01)); // Single
        assertEquals(Optional.empty(), Util.ListUtil.findOne(IPred2.vertexSame(), new ArrayList<>(List.of(V01)), V02)); // Single - No Find
        assertEquals(Optional.of(V03), Util.ListUtil.findOne(IPred2.vertexSame(), new ArrayList<>(List.of(V01, V02, V03)), V03)); // Multi
        assertEquals(Optional.empty(), Util.ListUtil.findOne(IPred2.vertexSame(), new ArrayList<>(List.of(V01, V02, V03)), V06)); // Multi - No Find
    }

    @Test
    public void utilfindIfExclude() {
        assertEquals(new ArrayList<>(), Util.ListUtil.findIfExclude(IPred2.vertexSame(), new ArrayList<>(), new ArrayList<>())); // Empty test
        assertEquals(new ArrayList<>(List.of(V01)), Util.ListUtil.findIfExclude(IPred2.vertexSame(), new ArrayList<>(List.of(V01)), new ArrayList<>())); // Empty test
        assertEquals(new ArrayList<>(), Util.ListUtil.findIfExclude(IPred2.vertexSame(), new ArrayList<>(), new ArrayList<>(List.of(V01)))); // Empty test

        assertEquals(new ArrayList<>(), Util.ListUtil.findIfExclude(IPred2.vertexSame(), new ArrayList<>(List.of(V01)), new ArrayList<>(List.of(V01)))); // Single - Equal test
        assertEquals(new ArrayList<>(List.of(V01)), Util.ListUtil.findIfExclude(IPred2.vertexSame(), new ArrayList<>(List.of(V01)), new ArrayList<>(List.of(V02)))); // Single - Non-Equal test

        assertEquals(new ArrayList<>(List.of(V01, V02, V03)), Util.ListUtil.findIfExclude(IPred2.vertexSame(), new ArrayList<>(List.of(V01, V02, V03)), new ArrayList<>(List.of(V06)))); // Multi - Non-Equal test
        assertEquals(new ArrayList<>(List.of(V02, V03)), Util.ListUtil.findIfExclude(IPred2.vertexSame(), new ArrayList<>(List.of(V01, V02, V03)), new ArrayList<>(List.of(V01)))); // Multi - Non-Equal test
        assertEquals(new ArrayList<>(List.of(V01, V02)), Util.ListUtil.findIfExclude(IPred2.vertexSame(), new ArrayList<>(List.of(V01, V02, V03)), new ArrayList<>(List.of(V03)))); // Multi - Non-Equal test
        assertEquals(new ArrayList<>(List.of(V02)), Util.ListUtil.findIfExclude(IPred2.vertexSame(), new ArrayList<>(List.of(V01, V02, V03)), new ArrayList<>(List.of(V01, V03)))); // Multi - Non-Equal test
    }

    @Test
    public void mapTest() {
        List<Integer> LOI00 = new ArrayList<Integer>();
        List<Integer> LOI01 = new ArrayList<>(List.of(1));
        List<Integer> LOI03 = new ArrayList<>(List.of(1,2,3));
        assertEquals(new ArrayList<String>(), Util.ListUtil.map(new IntToString(), LOI00));
        assertEquals(new ArrayList<String>(List.of("1")), Util.ListUtil.map(new IntToString(), LOI01));
        assertEquals(new ArrayList<String>(List.of("1", "2", "3")), Util.ListUtil.map(new IntToString(), LOI03));
    }

    @Test
    public void foldrTest() {
        List<String> LOI00 = new ArrayList<>();
        List<String> LOI01 = new ArrayList<>(List.of("1"));
        List<String> LOI03 = new ArrayList<>(List.of("1","2","3"));
        assertEquals("", Util.ListUtil.foldr(new Concat(), LOI00, ""));
        assertEquals("1", Util.ListUtil.foldr(new Concat(), LOI01, ""));
        assertEquals("321", Util.ListUtil.foldr(new Concat(), LOI03, ""));
    }

    @Test
    public void foldlTest() {
        List<String> LOI00 = new ArrayList<>();
        List<String> LOI01 = new ArrayList<>(List.of("1"));
        List<String> LOI03 = new ArrayList<>(List.of("1","2","3"));
        assertEquals("", Util.ListUtil.foldl(new Concat(), LOI00, ""));
        assertEquals("1", Util.ListUtil.foldl(new Concat(), LOI01, ""));
        assertEquals("123", Util.ListUtil.foldl(new Concat(), LOI03, ""));
    }

    // -------------------------------------
    // isMove()
    // -------------------------------------

    // Regular case:
    @Test
    public void isMove() {
        assertFalse(Util.GameUtil.isMove(DP00, PAL00)); // Draw Empty, pyramid empty

        assertTrue(Util.GameUtil.isMove(DP00, PAL01M)); // Draw Empty, pyramid single move
        assertFalse(Util.GameUtil.isMove(DP00, PAL01NM)); // Draw Empty, pyramid single no move

        assertTrue(Util.GameUtil.isMove(DP00, PAL03M)); // Draw Empty, moves in pyramid
        assertTrue(Util.GameUtil.isMove(DP00, PAL05M)); // Draw Empty, moves in pyramid
        assertTrue(Util.GameUtil.isMove(DP00, PAL05M)); // Draw Empty, moves in pyramid
        assertFalse(Util.GameUtil.isMove(DP00, PAL03NM)); // Draw Empty, pyramid multi no moves

        assertFalse(Util.GameUtil.isMove(DP01M, PAL00)); // Draw Single, pyramid empty
        assertFalse(Util.GameUtil.isMove(DP01NM, PAL00)); // Draw Single, pyramid empty

        assertFalse(Util.GameUtil.isMove(DP03M, PAL00)); // Draw multi, pyramid empty
        assertFalse(Util.GameUtil.isMove(DP03NM, PAL00)); // Draw multi, pyramid empty

        assertFalse(Util.GameUtil.isMove(DP01M,
                new ArrayList<>(
                        List.of(new Card(CardType.Queen, Suit.Club))))); // Draw single, pyramid single
        assertTrue(Util.GameUtil.isMove(DP01NM,
                new ArrayList<>(
                    List.of(new Card(CardType.Queen, Suit.Club))))); // Draw single, pyramid single

        assertFalse(Util.GameUtil.isMove(DP03NM,
                new ArrayList<>(
                        List.of(new Card(CardType.Queen, Suit.Club))))); // Draw multi, pyramid single
        assertTrue(Util.GameUtil.isMove(DP03NM,
                new ArrayList<>(
                        List.of(new Card(CardType.Jack, Suit.Club))))); // Draw multi, pyramid single
        assertFalse(Util.GameUtil.isMove(DP03NM,
                new ArrayList<>(
                        List.of(new Card(CardType.Queen, Suit.Club),
                                new Card(CardType.Seven, Suit.Club),
                                new Card(CardType.Five, Suit.Club))))); // Draw multi, pyramid multi
        assertTrue(Util.GameUtil.isMove(DP03NM,
                new ArrayList<>(
                        List.of(new Card(CardType.Jack, Suit.Club),
                                new Card(CardType.Seven, Suit.Club),
                                new Card(CardType.Five, Suit.Club))))); // Draw multi, pyramid multi
        assertTrue(Util.GameUtil.isMove(DP03NM,
                new ArrayList<>(
                        List.of(new Card(CardType.Queen, Suit.Club),
                                new Card(CardType.Seven, Suit.Club),
                                new Card(CardType.Jack, Suit.Club))))); // Draw multi, pyramid multi

    }


    class IntToString implements Function<Integer, String> {
        public String apply(Integer i) {
            return i.toString();
        }
    }

    class Concat implements BiFunction<String, String, String> {
        public String apply(String s1, String s2) {
            return s2 + s1;
        }
    }
}