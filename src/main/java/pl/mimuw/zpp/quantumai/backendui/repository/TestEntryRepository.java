package pl.mimuw.zpp.quantumai.backendui.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestEntryRepository extends MongoRepository<TestEntry, String> {
}
