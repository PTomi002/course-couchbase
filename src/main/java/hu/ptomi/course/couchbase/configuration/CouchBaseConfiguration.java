package hu.ptomi.course.couchbase.configuration;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ReactiveCollection;
import com.couchbase.transactions.TransactionDurabilityLevel;
import com.couchbase.transactions.Transactions;
import com.couchbase.transactions.config.TransactionConfigBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.context.annotation.Bean;
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

    @Bean
    public ReactiveCollection projects(final Cluster cluster) {
        return cluster
                .bucket(bucket)
                .scope("projects")
                .collection("project")
                .reactive();
    }

    @Bean
    public ReactiveCollection tasks(final Cluster cluster) {
        return cluster
                .bucket(bucket)
                .scope("projects")
                .collection("task")
                .reactive();
    }

    @Bean
    public Transactions transactions(final Cluster cluster) {
        return Transactions
                .create(
                        cluster,
                        TransactionConfigBuilder
                                .create()
                                // MAJORITY by default, with 1 replica it causes exception
                                .durabilityLevel(TransactionDurabilityLevel.NONE)
                                .build()
                );
    }
}
