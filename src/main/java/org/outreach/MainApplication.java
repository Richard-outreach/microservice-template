package org.outreach;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import org.jooby.Jooby;
import org.jooby.Route;
import org.jooby.apitool.ApiTool;
import org.jooby.json.Gzon;
import org.jooby.metrics.HealthCheckHandler;
import org.jooby.metrics.Metrics;

import java.util.concurrent.TimeUnit;


public class MainApplication extends Jooby {

    {

        use(new Gzon());
        use(BeerController.class);
        use(new ApiTool()
                .swagger("/swagger")
        );
        use(new Metrics()
                .request()
                .threadDump()
                .ping()
                .healthCheck("db", new Check())
                .metric("memory", new MemoryUsageGaugeSet())
                .metric("threads", new ThreadStatesGaugeSet())
                .metric("gc", new GarbageCollectorMetricSet()).reporter(registry -> {
                    ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();
                    reporter.start(1, TimeUnit.MINUTES);
                    return reporter;
                })
        );
    }

    private static class Check extends HealthCheck {

        @Override
        protected Result check() throws Exception {
            return Result.healthy();
        }
    }

    public static void main(String[] args) {
        run(MainApplication::new, args);
	}

}
