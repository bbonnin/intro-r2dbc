package io.millesabords.r2dbc;

import io.r2dbc.proxy.core.MethodExecutionInfo;
import io.r2dbc.proxy.core.QueryExecutionInfo;
import io.r2dbc.proxy.core.ValueStore;
import io.r2dbc.proxy.listener.ProxyMethodExecutionListener;
import io.r2dbc.proxy.support.MethodExecutionInfoFormatter;
import io.r2dbc.proxy.support.QueryExecutionInfoFormatter;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;

@Slf4j
public class StatementExecListener implements ProxyMethodExecutionListener {

    private final QueryExecutionInfoFormatter queryFormatter = new QueryExecutionInfoFormatter()
            .showTime()
            .showConnection()
            .showQuery()
            .showBindings();

    private final MethodExecutionInfoFormatter methodFormatter = MethodExecutionInfoFormatter.withDefault();

    private final Clock clock;

    public StatementExecListener() {
        this.clock = Clock.systemDefaultZone();
    }

    // Demo: utilisation du ValueStore pour stocker de l'information sur l'exécution
    @Override
    public void beforeExecuteOnStatement(MethodExecutionInfo methodInfo) {
        ValueStore store = methodInfo.getValueStore();
        store.put("before-exec-stmt", clock.instant());
    }

    @Override
    public void afterExecuteOnStatement(MethodExecutionInfo methodExecutionInfo) {
        log.info(methodFormatter.format(methodExecutionInfo));

    }

    @Override
    public void afterExecuteOnStatement(QueryExecutionInfo queryExecutionInfo) {
        log.info(queryFormatter.format(queryExecutionInfo));
    }
}
