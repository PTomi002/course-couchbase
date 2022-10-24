package hu.ptomi.course.couchbase.repository;

import hu.ptomi.course.couchbase.model.Project;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// #{#n1ql.selectEntity} = "SELECT * FROM project"
// #{#n1ql.filter} = "`_class` = 'hu.ptomi.course.couchbase.model.Project'"
// mandatory query properties:
//      META(p).`id` AS __id
//      META(p).`cas` AS __cas
@Repository
public interface ProjectRepository extends ReactiveCouchbaseRepository<Project, String> {
    // (1) simple queries
    // CREATE INDEX `find_by_name_index` ON `couchbase_course_bucket`.`projects`.`project`(name, _class) WHERE _class='hu.ptomi.course.couchbase.model.Project'
    // SELECT * FROM project p WHERE p.name="ProjectC" AND _class="hu.ptomi.course.couchbase.model.Project" ORDER BY cost ASC
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND name = $1 ORDER BY cost ASC")
    Flux<Project> findByName(String name);

    // SELECT * FROM project p WHERE p.name LIKE "%C%" AND _class="hu.ptomi.course.couchbase.model.Project" ORDER BY cost ASC
    Flux<Project> findByNameLikeOrderByEstimatedCostAsc(String name);

    // (2) array indexing and queries
    // CREATE INDEX find_if_contains_country ON `project`(DISTINCT ARRAY `country` FOR `country` IN `countryList` END) WHERE `_class` = 'hu.ptomi.course.couchbase.model.Project'
    // SELECT * FROM `project` WHERE `_class` = "hu.ptomi.course.couchbase.model.Project" AND (SOME country IN countryList SATISFIES country = "UK" END)
    //      (*) SOME is an alias to ANY
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND (SOME country IN countryList SATISFIES country = $1 END)")
    Flux<Project> findIfContainsCountry(String country);

    // (3) nested queries
    // SELECT * FROM `project` WHERE `_class` = "hu.ptomi.course.couchbase.model.Project" AND META().id IN (SELECT RAW t.pid FROM `task` t WHERE `_class` = "hu.ptomi.course.couchbase.model.Task" AND t.name = "BTask1")
    //      (*) SELECT RAW does not generate qualifier for the response set
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND META().id IN (SELECT RAW t.pid FROM `task` t WHERE `_class` = 'hu.ptomi.course.couchbase.model.Task' AND t.name = $1)")
    Flux<Project> findByTaskName(String name);

    // (4) join queries
    // CREATE INDEX `find_by_task_cost` ON `couchbase_course_bucket`.`projects`.`task`(`cost`,`pid`) WHERE (`_class` = "hu.ptomi.course.couchbase.model.Task")
    //      (*) provide much better perf. than a normal index on just _class or on pid
    // SELECT p FROM project p JOIN task t ON t.pid = META(p).id WHERE p.`_class` = 'hu.ptomi.course.couchbase.model.Project' AND t.`_class` = 'hu.ptomi.course.couchbase.model.Task' AND t.cost >= 100
    @Query("SELECT p.*, META(p).`id` AS __id, META(p).`cas` AS __cas FROM project p JOIN task t ON t.pid = META(p).id WHERE p.`_class` = 'hu.ptomi.course.couchbase.model.Project' AND t.`_class` = 'hu.ptomi.course.couchbase.model.Task' AND t.cost >= $1")
    Flux<Project> findByTaskCost(int cost);

    // (5) spring data projection
    // SELECT p.name, p.cost, META(p).`id` AS __id, META(p).`cas` AS __cas FROM project p WHERE `_class` = 'hu.ptomi.course.couchbase.model.Project' AND name = $1
    @Query("SELECT p._class, p.name, p.cost, META(p).`id` AS __id, META(p).`cas` AS __cas FROM project p WHERE `_class` = 'hu.ptomi.course.couchbase.model.Project' AND name = $1")
    Mono<Project> simpleProjectViewByName(String name);
}
