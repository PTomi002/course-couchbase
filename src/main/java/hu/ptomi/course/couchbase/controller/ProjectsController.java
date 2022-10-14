package hu.ptomi.course.couchbase.controller;

import hu.ptomi.course.couchbase.model.Project;
import hu.ptomi.course.couchbase.model.Task;
import hu.ptomi.course.couchbase.service.ProjectsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
public class ProjectsController {
    private final ProjectsService projectsService;

    public ProjectsController(ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    @PostMapping(path = "/projects")
    public Mono<ResponseEntity<Project>> createProject(
            @Valid @RequestBody Project project,
            UriComponentsBuilder b
    ) {
        return projectsService
                .createProject(project)
                .map(p ->
                        ResponseEntity
                                .created(b.path("projects/{id}").buildAndExpand(p.id()).toUri())
                                .body(p)
                );
    }

    @GetMapping(path = "/projects")
    public Flux<Project> findAllProject() {
        return projectsService.findAllProject();
    }

    @GetMapping(path = "/projects/{id}")
    public Mono<ResponseEntity<Project>> findProjectById(@PathVariable String id) {
        return projectsService
                .findProjectById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.noContent().build()));
    }

    @PostMapping(path = "/tasks")
    public Mono<ResponseEntity<Task>> createTask(
            @Valid @RequestBody Task task,
            UriComponentsBuilder b
    ) {
        return projectsService
                .createTask(task)
                .map(t ->
                        ResponseEntity
                                .created(b.path("tasks/{id}").buildAndExpand(t.id()).toUri())
                                .body(t)
                );
    }

    @GetMapping(path = "/tasks")
    public Flux<Task> findAllTask() {
        return projectsService.findAllTask();
    }

    @GetMapping(path = "/tasks/{id}")
    public Mono<ResponseEntity<Task>> findTaskById(@PathVariable String id) {
        return projectsService
                .findTaskById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.noContent().build()));
    }
}
