package hu.ptomi.course.couchbase.repository;

import hu.ptomi.course.couchbase.model.Project;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends ReactiveCouchbaseRepository<Project, String> {
    // findAll generates a Nickel query:
    // SELECT *
    // FROM project
    // WHERE _class="hu.ptomi.course.couchbase.model.Project"

    // create primary index:
    // CREATE PRIMARY INDEX `couchbase_course_bucket_project_primary_index` ON `couchbase_course_bucket`.`projects`.`project`
    // CREATE PRIMARY INDEX `couchbase_course_bucket_task_primary_index` ON `couchbase_course_bucket`.`projects`.`task`

    // create secondary index:
    // CREATE INDEX `couchbase_course_bucket_task_index` ON `couchbase_course_bucket`.`projects`.`task`(_class)
    //      WHERE _class='hu.ptomi.course.couchbase.model.Task'
    // CREATE INDEX `couchbase_course_bucket_project_index` ON `couchbase_course_bucket`.`projects`.`project`(_class)
    //      WHERE _class='hu.ptomi.course.couchbase.model.Project'
}
