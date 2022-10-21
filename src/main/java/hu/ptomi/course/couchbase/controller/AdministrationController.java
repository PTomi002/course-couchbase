package hu.ptomi.course.couchbase.controller;

import hu.ptomi.course.couchbase.model.Project;
import hu.ptomi.course.couchbase.model.Task;
import hu.ptomi.course.couchbase.service.AdministrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static java.util.Objects.isNull;

@RestController
public class AdministrationController {
    private final AdministrationService administrationService;

    @Autowired
    public AdministrationController(AdministrationService administrationService) {
        this.administrationService = administrationService;
    }

    @PostMapping(path = "/projects")
    public Mono<ResponseEntity<Project>> createProject(
            @Valid @RequestBody Project project,
            UriComponentsBuilder b
    ) {
        return administrationService
                .createProject(project)
                .map(p ->
                        ResponseEntity
                                .created(b.path("projects/{id}").buildAndExpand(p.id()).toUri())
                                .body(p)
                );
    }

    @GetMapping(path = "/projects/{id}")
    public Mono<ResponseEntity<Project>> findProjectById(@PathVariable String id) {
        return administrationService
                .findProjectById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.noContent().build()));
    }

    @GetMapping(path = "/projects")
    public Flux<Project> findProjectsBy(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "like", required = false, defaultValue = "false") boolean isLike
    ) {
        if (isNull(name) || name.isBlank())
            return administrationService.findAllProject();
        else if (isLike)
            return administrationService.findProjectByNameLike("%" + name + "%");
        else
            return administrationService.findProjectByName(name);
    }

    @PostMapping(path = "/tasks")
    public Mono<ResponseEntity<Task>> createTask(
            @Valid @RequestBody Task task,
            UriComponentsBuilder b
    ) {
        return administrationService
                .createTask(task)
                .map(t ->
                        ResponseEntity
                                .created(b.path("tasks/{id}").buildAndExpand(t.id()).toUri())
                                .body(t)
                );
    }

    @GetMapping(path = "/tasks")
    public Flux<Task> findTasksBy() {
        return administrationService.findAllTask();
    }

    @GetMapping(path = "/tasks/{id}")
    public Mono<ResponseEntity<Task>> findTaskById(@PathVariable String id) {
        return administrationService
                .findTaskById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.noContent().build()));
    }
}
