package pl.mimuw.zpp.quantumai.backendui.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.mimuw.zpp.quantumai.backendui.model.GraphPackage;

@Repository
public interface GraphPackageRepository extends MongoRepository<GraphPackage, String> {
}
