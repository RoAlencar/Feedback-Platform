package br.com.fiap.feedback.adapter.output.persistence.repository;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;

public class PostgresTestResource implements QuarkusTestResourceLifecycleManager {

    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("feedback_test")
                    .withUsername("test")
                    .withPassword("test");

    @Override
    public Map<String, String> start() {
        postgres.start();

        return Map.of(
                "quarkus.datasource.db-kind", "postgresql",
                "quarkus.datasource.jdbc.url", postgres.getJdbcUrl(),
                "quarkus.datasource.username", postgres.getUsername(),
                "quarkus.datasource.password", postgres.getPassword(),

                // IMPORTANTE: agora Flyway roda igual prod
                "quarkus.flyway.migrate-at-start", "true",

                // deixa schema controlado pelo Flyway
                "quarkus.hibernate-orm.database.generation", "none"
        );
    }

    @Override
    public void stop() {
        postgres.stop();
    }
}