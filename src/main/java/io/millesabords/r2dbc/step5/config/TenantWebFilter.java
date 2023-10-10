package io.millesabords.r2dbc.step5.config;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class TenantWebFilter implements WebFilter {

    private static final String TENANT_ID_HEADER = "X-TENANT-ID";


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        // L'id du tenant est dans le header X-TENANT-ID

        return chain
                .filter(exchange)
                .contextWrite(context ->
                        TenantIdHolder.withTenantId(exchange.getRequest().getHeaders().getFirst(TENANT_ID_HEADER)));
    }
}
