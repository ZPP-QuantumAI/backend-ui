package pl.mimuw.zpp.quantumai.backendui.model;

import io.vavr.control.Either;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

public class EuclideanGraph implements WeightedGraph {
    private final List<NodeCoordinates> nodes;

    public EuclideanGraph() {
        this.nodes = new ArrayList<>();
    }

    public void addNode(BigDecimal x, BigDecimal y) {
        nodes.add(new NodeCoordinates(x, y));
    }

    @Override
    public int getNumberOfVertices() {
        return nodes.size();
    }

    @Override
    public Either<String, BigDecimal> getWeight(int a, int b) {
        if (verticesAreInvalid(a, b)) return invalidVerticesMessage();
        return Either.right(distance(nodes.get(a), nodes.get(b)));
    }

    private static BigDecimal distance(NodeCoordinates coord1, NodeCoordinates coord2) {
        BigDecimal deltaXSquared = coord1.x.subtract(coord2.x).pow(2),
                deltaYSquared = coord1.y.subtract(coord2.y).pow(2);
        return deltaXSquared.add(deltaYSquared).sqrt(MathContext.DECIMAL128);
    }

    private record NodeCoordinates(
            BigDecimal x,
            BigDecimal y
    ) {}
}
