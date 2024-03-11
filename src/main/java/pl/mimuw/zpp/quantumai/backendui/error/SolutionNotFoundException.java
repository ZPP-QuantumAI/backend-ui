package pl.mimuw.zpp.quantumai.backendui.error;

import lombok.Getter;

public class SolutionNotFoundException extends RuntimeException {
    @Getter private final String solutionId;

    public SolutionNotFoundException(String solutionId) {
        super();
        this.solutionId = solutionId;
    }
}
