package hu.ptomi.course.couchbase.service;

import hu.ptomi.course.couchbase.model.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {
    Mono<Task> createTask(Task task);

    Mono<Task> findTaskById(String id);

    Flux<Task> findAllTask();
}
