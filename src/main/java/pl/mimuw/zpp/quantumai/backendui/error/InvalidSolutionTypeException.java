package pl.mimuw.zpp.quantumai.backendui.error;

import lombok.Getter;
import pl.mimuw.zpp.quantumai.backendui.model.Solution;

public class InvalidSolutionTypeException extends RuntimeException {
    @Getter private final Solution.SolutionType expectedType;
    @Getter private final Solution.SolutionType actualType;

    public InvalidSolutionTypeException(
            Solution.SolutionType expectedType,
            Solution.SolutionType actualType
    ) {
        super();
        this.expectedType = expectedType;
        this.actualType = actualType;
    }
}
