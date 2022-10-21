package hu.ptomi.course.couchbase.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;

@Configuration
public class CouchBaseConfiguration extends AbstractCouchbaseConfiguration {
    private final CouchbaseProperties couchbaseProperties;
    private final String bucket;

    @Autowired
    public CouchBaseConfiguration(
            @Value("${spring.couchbase.bucket}") String bucket,
            CouchbaseProperties couchbaseProperties
    ) {
        this.couchbaseProperties = couchbaseProperties;
        this.bucket = bucket;
    }

    @Override
    public String getConnectionString() {
        return couchbaseProperties.getConnectionString();
    }

    @Override
    public String getUserName() {
        return couchbaseProperties.getUsername();
    }

    @Override
    public String getPassword() {
        return couchbaseProperties.getPassword();
    }

    @Override
    public String getBucketName() {
        return bucket;
    }
}
