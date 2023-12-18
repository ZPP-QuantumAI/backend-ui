package pl.mimuw.zpp.quantumai.backendui.verifier.result;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record TspResult(
        BigDecimal sumOfWeights,
        List<Integer> permutation
) {
}
