package hu.ptomi.course.couchbase.controller.errorhandler;

import hu.ptomi.course.couchbase.controller.ProjectsController;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice(basePackageClasses = {ProjectsController.class})
public class ProjectsErrorHandler {

    @ExceptionHandler(value = {DuplicateKeyException.class})
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public Map<String, Object> resourceAlreadyExist(DuplicateKeyException ex) {
        return Map.of(
                "error", ex.getClass().getName(),
                "message", ex.getRootCause().getMessage()
        );
    }
}
