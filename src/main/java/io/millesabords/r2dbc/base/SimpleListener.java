package io.millesabords.r2dbc.base;

import io.r2dbc.proxy.core.QueryExecutionInfo;
import io.r2dbc.proxy.listener.ProxyMethodExecutionListener;
import io.r2dbc.proxy.support.QueryExecutionInfoFormatter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleListener implements ProxyMethodExecutionListener {

    private QueryExecutionInfoFormatter queryFormatter = new QueryExecutionInfoFormatter()
            .showTime()
            .showConnection()
            .showQuery();

    @Override
    public void afterExecuteOnStatement(QueryExecutionInfo queryExecutionInfo) {
        log.info(queryFormatter.format(queryExecutionInfo));
    }
}
