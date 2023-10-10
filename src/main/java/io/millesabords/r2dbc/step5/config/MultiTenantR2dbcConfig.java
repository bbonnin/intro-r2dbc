package io.millesabords.r2dbc.step5.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableR2dbcRepositories(
        basePackages = "io.millesabords.r2dbc.step5")
public class MultiTenantR2dbcConfig extends AbstractR2dbcConfiguration {

    private final Map<String, ConnectionFactory> r2dbcConnectionFactories = new HashMap<>();

    private final R2dbcProperties r2dbcProperties;

    private final AppConfig appConfig;

    @PostConstruct
    public void createConnectionFactories() {
        appConfig.getTenants().forEach(tenant ->
            r2dbcConnectionFactories.put(tenant, ConnectionFactories.get(buildConnectionFactoryOptions(tenant))));
    }

    @Override
    @Bean("connectionFactory")
    public ConnectionFactory connectionFactory() {
        TenantRoutingConnectionFactory connectionFactory = new TenantRoutingConnectionFactory();
        connectionFactory.setDefaultTargetConnectionFactory(
                ConnectionFactories.get(buildConnectionFactoryOptions(null)));
        connectionFactory.setTargetConnectionFactories(r2dbcConnectionFactories);
        return connectionFactory;
    }

    private ConnectionFactoryOptions buildConnectionFactoryOptions(String schema) {
        String url = r2dbcProperties.getUrl() + (schema == null ? "" : "SCHEMA=" + schema); // H2
        return ConnectionFactoryOptions.builder()
                .from(ConnectionFactoryOptions.parse(url))
                .option(ConnectionFactoryOptions.USER, r2dbcProperties.getUsername())
                .option(ConnectionFactoryOptions.PASSWORD, r2dbcProperties.getPassword())
                .build();
    }
}
