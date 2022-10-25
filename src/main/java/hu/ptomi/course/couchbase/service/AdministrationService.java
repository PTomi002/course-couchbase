package hu.ptomi.course.couchbase.service;

import com.couchbase.client.core.cnc.events.transaction.TransactionLogEvent;
import com.couchbase.client.java.ReactiveCollection;
import com.couchbase.client.java.transactions.error.TransactionCommitAmbiguousException;
import com.couchbase.client.java.transactions.error.TransactionFailedException;
import com.couchbase.transactions.Transactions;
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

import java.util.UUID;

@Service
public class AdministrationService implements ProjectService, TaskService {

    private final Logger logger = LoggerFactory.getLogger("QUERY");
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    // these belong to the repo layer, but no intent to do it as keeping things simple as can be
    private final Transactions transactions;
    private final ReactiveCollection projects;
    private final ReactiveCollection tasks;

    @Autowired
    public AdministrationService(
            ProjectRepository projectRepository,
            TaskRepository taskRepository,
            Transactions transactions,
            ReactiveCollection projects,
            ReactiveCollection tasks
    ) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.transactions = transactions;
        this.projects = projects;
        this.tasks = tasks;
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
                .then(
                        Mono.defer(() ->
                                transactions.reactive()
                                        .run(ctx -> {
                                            var id = "project_" + UUID.randomUUID();
                                            return ctx
                                                    .insert(projects, id, generateProject())
                                                    .then(ctx.insert(tasks, "task_" + UUID.randomUUID(), generateTask(id)))
                                                    .then(ctx.commit());
                                            // auto-rollback after lambda runs
                                        })
                                        .doOnError(err -> {
                                            if (err instanceof TransactionCommitAmbiguousException) {
                                                logCommitAmbiguousError((TransactionCommitAmbiguousException) err);
                                            } else if (err instanceof TransactionFailedException) {
                                                logFailure((TransactionFailedException) err);
                                            }
                                        })
                        )
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

    private Project generateProject() {
        return Project.builder()
                .name("name_" + UUID.randomUUID())
                .type("project")
                .build();
    }

    private Task generateTask(String pid) {
        // throw new RuntimeException("injected error to see rollback");
        return Task.builder()
                .name("name_" + UUID.randomUUID())
                .projectId(pid)
                .type("task")
                .build();
    }

    private void logCommitAmbiguousError(TransactionCommitAmbiguousException err) {
        logger.warn("Transaction possibly reached the commit point");
        for (TransactionLogEvent msg : err.logs()) {
            logger.warn(msg.toString());
        }
    }

    private void logFailure(TransactionFailedException err) {
        logger.warn("Transaction did not reach commit point");
        for (TransactionLogEvent msg : err.logs()) {
            logger.warn(msg.toString());
        }
    }
}
