package javax.finance.stockquotes.test;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {

    private static final PostgresTestContainer SHARED_POSTGRES_TEST_CONTAINER =
            new PostgresTestContainer().withReuse(true);

    private PostgresTestContainer() {
        super("postgres:14.1-alpine");
    }

    public static PostgresTestContainer getInstance() {
        return SHARED_POSTGRES_TEST_CONTAINER;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", SHARED_POSTGRES_TEST_CONTAINER.getJdbcUrl());
        System.setProperty("DB_USERNAME", SHARED_POSTGRES_TEST_CONTAINER.getUsername());
        System.setProperty("DB_PASSWORD", SHARED_POSTGRES_TEST_CONTAINER.getPassword());
    }

    @Override
    public void stop() {
        // JVM handles shut down
    }

}