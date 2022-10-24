package hu.ptomi.course.couchbase.service;

import hu.ptomi.course.couchbase.model.Project;
import hu.ptomi.course.couchbase.model.Task;
import hu.ptomi.course.couchbase.repository.ProjectRepository;
import hu.ptomi.course.couchbase.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AdministrationService implements ProjectService, TaskService {

    private final Logger logger = LoggerFactory.getLogger("QUERY");
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public AdministrationService(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    public Mono<Void> runOtherEndpoints() {
        return projectRepository
                .count()
                .doOnNext(count -> logger.info("count: " + count))
                .then(
                        projectRepository
                                .findIfContainsCountry("UK")
                                .collectList()
                                .doOnNext(coll -> logger.info("findIfContainsCountry: " + coll))
                )
                .then(
                        projectRepository
                                .findByTaskName("BTask1")
                                .collectList()
                                .doOnNext(coll -> logger.info("findByTaskName: " + coll))
                )
                .then(
                        projectRepository
                                .findByTaskCost(100)
                                .collectList()
                                .doOnNext(coll -> logger.info("findByTaskCost: " + coll))
                )
                .then(
                        projectRepository
                                .simpleProjectViewByName("ProjectA")
                                .doOnNext(coll -> logger.info("simpleProjectViewByName: " + coll))
                )
                .then();
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
    public Flux<Project> findProjectByName(String name) {
        return projectRepository.findByName(name);
    }

    @Override
    public Flux<Project> findProjectByNameLike(String name) {
        return projectRepository.findByNameLikeOrderByEstimatedCostAsc("%" + name + "%");
    }

    @Override
    public Flux<Task> findAllTask() {
        return taskRepository.findAll();
    }
}
