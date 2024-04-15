package pl.mimuw.zpp.quantumai.backendui.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.mimuw.zpp.quantumai.backendui.model.Problem;
import pl.mimuw.zpp.quantumai.backendui.model.Solution;

import java.util.List;

@Repository
public interface SolutionRepository extends MongoRepository<Solution, String> {

    List<Solution> findAllByProblem(Problem problem);
}
