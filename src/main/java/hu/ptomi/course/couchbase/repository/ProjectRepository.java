package hu.ptomi.course.couchbase.repository;

import hu.ptomi.course.couchbase.model.Project;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends ReactiveCouchbaseRepository<Project, String> {
}
