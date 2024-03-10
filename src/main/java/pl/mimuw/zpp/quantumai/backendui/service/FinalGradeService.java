package pl.mimuw.zpp.quantumai.backendui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mimuw.zpp.quantumai.backendui.finalgrade.FinalGrader;
import pl.mimuw.zpp.quantumai.backendui.model.Grade;
import pl.mimuw.zpp.quantumai.backendui.model.Problem;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FinalGradeService {
    private final Map<Problem, FinalGrader> finalGraderMap;

    public Object finalGrade(List<Grade> grades) {
        Problem problem = grades.stream()
                .map(Grade::problem)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("list of grades should not be empty"));
        if (!areAllGradesForSameProblem(grades, problem))
            throw new RuntimeException("some grades are not for proper problem");
        FinalGrader grader = finalGraderMap.get(problem);
        return grader.finalGrade(grades);
    }

    private boolean areAllGradesForSameProblem(List<Grade> grades, Problem problem) {
        return grades.stream()
                .map(Grade::problem)
                .allMatch(problem::equals);
    }
}
