package hu.ptomi.course.couchbase.service;

import hu.ptomi.course.couchbase.model.Project;
import hu.ptomi.course.couchbase.model.Task;
import hu.ptomi.course.couchbase.repository.ProjectRepository;
import hu.ptomi.course.couchbase.repository.TaskRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProjectsServiceImpl implements ProjectsService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public ProjectsServiceImpl(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public Mono<Project> createProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Mono<Task> createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Mono<Project> findProjectById(String id) {
        return projectRepository.findById(id);
    }

    @Override
    public Mono<Task> findTaskById(String id) {
        return taskRepository.findById(id);
    }

    @Override
    public Flux<Project> findAllProject() {
        return projectRepository.findAll();
    }

    @Override
    public Flux<Task> findAllTask() {
        return taskRepository.findAll();
    }
}
