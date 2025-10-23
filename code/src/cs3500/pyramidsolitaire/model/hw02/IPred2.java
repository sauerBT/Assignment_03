package cs3500.pyramidsolitaire.model.hw02;

public interface IPred2<T>{
    public boolean apply(T obj1, T obj2);

    public static <K> IPred2<Vertex<K>> vertexSame() { return new VertexSame<>(); }

    public static <K> IPred2<Vertex<K>> VertexDiff() { return new VertexDiff<>(); }

    public static IPred2<Card> equalsPred() { return new CardEquals(); }

    public static IPred2<Edge> equals() { return new EdgeSame(); }
}


class VertexSame<K> implements IPred2<Vertex<K>> {
    public boolean apply(Vertex<K> obj1, Vertex<K> obj2) {
        return obj1.sameSubject(obj2);
    }
}

class VertexDiff<K> implements IPred2<Vertex<K>> {
    public boolean apply(Vertex<K> obj1, Vertex<K> obj2) {
        return !obj1.sameSubject(obj2);
    }
}

class EdgeSame implements IPred2<Edge> {
    public boolean apply(Edge edge1, Edge edge2) {
        return edge1.equals(edge2);
    }
}

class CardEquals implements IPred2<Card> {
    public boolean apply(Card obj1, Card obj2) {
        return obj1.equals(obj2);
    }
}