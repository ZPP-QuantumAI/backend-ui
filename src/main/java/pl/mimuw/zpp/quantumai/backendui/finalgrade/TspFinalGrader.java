package pl.mimuw.zpp.quantumai.backendui.finalgrade;

import lombok.Builder;
import org.springframework.stereotype.Component;
import pl.mimuw.zpp.quantumai.backendui.model.Grade;
import pl.mimuw.zpp.quantumai.backendui.model.Problem;
import pl.mimuw.zpp.quantumai.backendui.verifier.result.TspResult;

import java.math.BigDecimal;
import java.util.List;

@Component
public class TspFinalGrader implements FinalGrader {
    @Override
    public Problem problem() {
        return Problem.TSP;
    }

    @Override
    public Object finalGrade(List<Grade> grades) {
        BigDecimal sumOfWeights = grades.stream()
                .map(Grade::result)
                .map(TspResult.class::cast)
                .map(TspResult::sumOfWeights)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return TspFinalGrade.builder()
                .sumOfWeights(sumOfWeights)
                .build();
    }

    @Builder
    public record TspFinalGrade(
           BigDecimal sumOfWeights
    ) {

    }
}
