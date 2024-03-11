package pl.mimuw.zpp.quantumai.backendui.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.mimuw.zpp.quantumai.backendui.model.Grade;

import java.util.List;

@Repository
public interface GradeRepository extends MongoRepository<Grade, String> {
    List<Grade> findBySolutionId(String solutionId);
}
