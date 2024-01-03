package pl.mimuw.zpp.quantumai.backendui.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.mimuw.zpp.quantumai.backendui.model.Grade;

@Repository
public interface GradeRepository extends MongoRepository<Grade, String> {
}
