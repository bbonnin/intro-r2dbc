package io.millesabords.r2dbc.step5.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(FlywayProperties.class)
@RequiredArgsConstructor
@Slf4j
public class FlywayConfig {

    private final AppConfig appConfig;

    private final FlywayProperties flywayProps;

    @PostConstruct
    public void runMigrations() {
        DataSource ds = DataSourceBuilder.create(this.getClass().getClassLoader())
                .url(flywayProps.getUrl())
                .username(flywayProps.getUser())
                .password(flywayProps.getPassword())
                .type(JdbcDataSource.class)
                .build();
        appConfig.getTenants().forEach(tenant -> runMigration(tenant, ds));
    }

    private void runMigration(String tenant, DataSource ds) {
        log.info("Run migration for '{}'", tenant);
        new Flyway(Flyway.configure()
                .schemas(tenant) // tenant name = schema
                .defaultSchema(tenant)
                .dataSource(ds)
                .locations(flywayProps.getLocations().toArray(String[]::new))
        ).migrate();
    }
}
