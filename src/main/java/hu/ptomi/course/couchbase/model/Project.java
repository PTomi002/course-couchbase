package hu.ptomi.course.couchbase.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.repository.Collection;
import org.springframework.data.couchbase.repository.Scope;

import java.util.List;

// should be generated from open api
// validated with @JsonProperty(required = "true", ...)
@Builder
@Document
@Scope(value = "projects")
@Collection(value = "project")
public record Project(
        // @GeneratedValue(strategy = GenerationStrategy.USE_ATTRIBUTES,delimiter="::")
        @Id String id,
        String name,
        String code,
        @Field("desc") String description,
        String startDate,
        String endDate,
        @Field("cost") long estimatedCost,
        List<String> countryList,
        // Version is handled by the db - Optimistic Locking
        @Version long version
) {
}
