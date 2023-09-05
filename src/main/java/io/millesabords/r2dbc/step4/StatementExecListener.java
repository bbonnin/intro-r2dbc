package io.millesabords.r2dbc.step4;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.r2dbc.proxy.core.QueryExecutionInfo;
import io.r2dbc.proxy.listener.ProxyMethodExecutionListener;
import io.r2dbc.proxy.support.QueryExecutionInfoFormatter;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

import static java.lang.Math.round;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Slf4j
public class StatementExecListener implements ProxyMethodExecutionListener {

    private final Duration slowQueryThreshold = Duration.ofMillis(10);

    private final QueryExecutionInfoFormatter queryFormatter = new QueryExecutionInfoFormatter()
            .showTime()
            .showConnection()
            .showQuery()
            .showBindings();

    public StatementExecListener() {
        Metrics.addRegistry(new SimpleMeterRegistry());
    }

    @Override
    public void beforeQuery(QueryExecutionInfo queryExecutionInfo) {
        // Something to do...
    }

    @Override
    public void afterQuery(QueryExecutionInfo queryExecutionInfo) {
        log.info(queryFormatter.format(queryExecutionInfo));

        Metrics.counter("r2dbc.query.count").increment();
        log.info(">>>> Query count: {}", round(Metrics.counter("r2dbc.query.count").count()));

        if (slowQueryThreshold.minus(queryExecutionInfo.getExecuteDuration()).isNegative()) {
            Timer slowTimer = Metrics.timer("r2dbc.query.slow.duration");
            slowTimer.record(queryExecutionInfo.getExecuteDuration());
            log.warn(">>>> Slow queries: count={}, mean={}ms, max={}ms",
                    round(slowTimer.count()), slowTimer.mean(MILLISECONDS), slowTimer.max(MILLISECONDS));
        }
    }
}
