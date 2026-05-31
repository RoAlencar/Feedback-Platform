package br.com.fiap.analytics.adapter.output.persistence.repository;

import java.util.Map;

import org.testcontainers.containers.PostgreSQLContainer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class PostgresTestResource implements QuarkusTestResourceLifecycleManager {

    @SuppressWarnings("resource")
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("analytics_test")
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
                "quarkus.flyway.migrate-at-start", "true",
                "quarkus.hibernate-orm.database.generation", "none");
    }

    @Override
    public void stop() {
        postgres.stop();
    }
}
