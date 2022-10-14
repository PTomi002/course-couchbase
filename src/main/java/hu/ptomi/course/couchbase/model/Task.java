package hu.ptomi.course.couchbase.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.repository.Collection;
import org.springframework.data.couchbase.repository.Scope;

@Document
@Scope(value = "projects")
@Collection(value = "task")
public record Task(
        @Id String id,
        @Field("pid") String projectId,
        String name,
        @Field("desc") String description,
        String ownerName,
        long cost,
        @Version long version
) {
}
