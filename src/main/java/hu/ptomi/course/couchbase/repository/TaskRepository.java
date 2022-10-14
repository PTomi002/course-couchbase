package hu.ptomi.course.couchbase.repository;

import hu.ptomi.course.couchbase.model.Task;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends ReactiveCouchbaseRepository<Task, String> {
}
