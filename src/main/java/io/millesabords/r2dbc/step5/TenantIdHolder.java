package io.millesabords.r2dbc.step5;

import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.function.Function;

public class TenantIdHolder {

    private static final String TENANT_ID_KEY = "TENANT_ID";

    public static Context withTenantId(String id) {
        return Context.of(TENANT_ID_KEY, Mono.just(id));
    }

    public static Mono<Object> getTenantId() {
        return Mono.deferContextual(
                contextView -> contextView.hasKey(TENANT_ID_KEY) ? contextView.get(TENANT_ID_KEY): Mono.empty());
    }

    public static Function<Context, Context> clearContext() {
        return (context) -> context.delete(TENANT_ID_KEY);
    }
}
