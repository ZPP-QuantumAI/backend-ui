package pl.mimuw.zpp.quantumai.backendui.model;

import io.vavr.control.Either;

import java.math.BigDecimal;
import java.util.Arrays;

public interface WeightedGraph {
    int getNumberOfVertices();
    Either<String, BigDecimal> getWeight(int a, int b);

    default boolean verticesAreInvalid(int... vertexNumbers) {
        return Arrays.stream(vertexNumbers)
                .filter(vertex -> vertex < 0 || vertex >= getNumberOfVertices())
                .findFirst()
                .isPresent();
    }

    default Either<String, BigDecimal> invalidVerticesMessage() {
        return Either.left("vertex number must be less or equal to the number of the number of vertices");
    }
}
