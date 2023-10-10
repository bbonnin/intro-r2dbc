package io.millesabords.r2dbc.step5.config;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.function.Function;

@Slf4j
public class TenantIdHolder {

    private static final String TENANT_ID_KEY = "TENANT-ID";

    public static Mono<Object> getTenantId() {
        return Mono.deferContextual(
                contextView -> contextView.hasKey(TENANT_ID_KEY) ? contextView.get(TENANT_ID_KEY): Mono.empty());
    }

    public static Function<Context, Context> clearContext() {
        return (context) -> context.delete(TENANT_ID_KEY);
    }

    public static Context withTenantId(String id) {
        log.info("Tenant id: {} ({})", id, Mono.justOrEmpty(id).block());
        return Context.of(TENANT_ID_KEY, Mono.justOrEmpty(id));
    }
}
