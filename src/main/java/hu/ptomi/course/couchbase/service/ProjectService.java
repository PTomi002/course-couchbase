package hu.ptomi.course.couchbase.service;

import hu.ptomi.course.couchbase.model.Project;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProjectService {
    Mono<Project> createProject(Project project);

    Mono<Project> findProjectById(String id);

    Flux<Project> findAllProject();

    Flux<Project> findProjectByName(String name);

    Flux<Project> findProjectByNameLike(String name);
}
