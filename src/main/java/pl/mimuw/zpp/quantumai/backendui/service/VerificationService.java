package pl.mimuw.zpp.quantumai.backendui.service;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mimuw.zpp.quantumai.backendui.model.EuclideanGraph;
import pl.mimuw.zpp.quantumai.backendui.model.Problem;
import pl.mimuw.zpp.quantumai.backendui.verifier.Verifier;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class VerificationService {
    private final Map<Problem, Verifier> verifierMap;
    public Either<String, Object> verify(
            EuclideanGraph graph,
            Problem problem,
            String programOutput
    ) {
        Verifier verifier = verifierMap.get(problem);
        return verifier.verify(graph, programOutput);
    }
}
