package hu.ptomi.course.couchbase.service;

import hu.ptomi.course.couchbase.model.Project;
import hu.ptomi.course.couchbase.model.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProjectsService {
    Mono<Project> createProject(Project project);

    Mono<Task> createTask(Task task);

    Mono<Project> findProjectById(String id);

    Mono<Task> findTaskById(String id);

    Flux<Project> findAllProject();

    Flux<Task> findAllTask();
}
