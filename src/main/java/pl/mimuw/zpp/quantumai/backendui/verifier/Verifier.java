package pl.mimuw.zpp.quantumai.backendui.verifier;

import io.vavr.control.Either;
import pl.mimuw.zpp.quantumai.backendui.model.EuclideanGraph;
import pl.mimuw.zpp.quantumai.backendui.model.Problem;

public interface Verifier {
    Problem problem();
    Either<String, Object> verify(EuclideanGraph graph, String programOutput);
}
