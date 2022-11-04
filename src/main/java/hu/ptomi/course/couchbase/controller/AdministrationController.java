package hu.ptomi.course.couchbase.controller;

import hu.ptomi.course.couchbase.model.Project;
import hu.ptomi.course.couchbase.model.Task;
import hu.ptomi.course.couchbase.service.AdministrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(AdministrationService.class);

    private final AdministrationService administrationService;

    @Autowired
    public AdministrationController(AdministrationService administrationService) {
        this.administrationService = administrationService;
    }

    // Do not want to create endpoint for each api call.
    @GetMapping("/test")
    public Mono<Void> runOtherEndpoints() {
        return administrationService.runOtherEndpoints();
    }

    // change project_1 to:
    // {
    //  "id": "project_1"
    //  "name": "ProjectA ProjectB" // add ProjectB to the name
    // }
    // {
    //  "id": "project_2"
    //  "name": "ProjectB"
    // }
    // {
    //  "id": "project_3"
    //  "name": "ProjectC"
    // }
    @GetMapping("/testFts")
    public Mono<Void> runFtsEndpoints() {
        return Mono
                .defer(() -> {
                    logger.info("fetch project_1");
                    return administrationService.
                            ftsMatchQueries("ProjectA")
                            .collectList();
                })
                .then(Mono.defer(() -> {
                            logger.info("fetch project_1 and project_2");
                            return administrationService
                                    .ftsMatchQueries("ProjectB")
                                    .collectList();
                        }
                ))
                .then(Mono.defer(() -> {
                            // this match query searches by terms!
                            logger.info("fetch project_1 and project_2");
                            return administrationService
                                    .ftsMatchQueries("ProjectA ProjectB")
                                    .collectList();
                        }
                ))
                .then(Mono.defer(() -> {
                            // this match query searches by whole phrase!
                            logger.info("fetch project_1");
                            return administrationService
                                    .ftsMatchPhraseQueries("ProjectA ProjectB")
                                    .collectList();
                        }
                ))
                .then(Mono.defer(() -> {
                            logger.info("fetch project_1 and project_2");
                            return administrationService
                                    .ftsMatchPhraseQueries("ProjectB")
                                    .collectList();
                        }
                ))
                .then();
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
            return administrationService.findProjectByNameLike(name);
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
