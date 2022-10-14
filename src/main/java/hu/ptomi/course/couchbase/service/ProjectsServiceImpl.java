package hu.ptomi.course.couchbase.service;

import hu.ptomi.course.couchbase.model.Project;
import hu.ptomi.course.couchbase.model.Task;
import hu.ptomi.course.couchbase.repository.ProjectRepository;
import hu.ptomi.course.couchbase.repository.TaskRepository;
import org.springframework.stereotype.Service;
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
}
