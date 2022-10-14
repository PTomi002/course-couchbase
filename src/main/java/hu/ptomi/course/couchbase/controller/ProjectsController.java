package hu.ptomi.course.couchbase.controller;

import hu.ptomi.course.couchbase.model.Project;
import hu.ptomi.course.couchbase.model.Task;
import hu.ptomi.course.couchbase.service.ProjectsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@RestController
public class ProjectsController {
    private final ProjectsService projectsService;

    public ProjectsController(ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    @PostMapping(path = "/projects")
    public Mono<ResponseEntity<Project>> createProject(
            @RequestBody Project project,
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

    @PostMapping(path = "/tasks")
    public Mono<ResponseEntity<Task>> createTask(
            @RequestBody Task task,
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
}
