package pl.mimuw.zpp.quantumai.backendui.finalgrade;

import pl.mimuw.zpp.quantumai.backendui.model.Grade;
import pl.mimuw.zpp.quantumai.backendui.model.Problem;

import java.util.List;

public interface FinalGrader {
    Problem problem();
    Object finalGrade(List<Grade> grades);
}
