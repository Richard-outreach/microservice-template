package org.outreach;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import org.jooby.Jooby;
import org.jooby.apitool.ApiTool;
import org.jooby.json.Gzon;
import org.jooby.metrics.Metrics;


public class MainApplication extends Jooby {

    {

        use(new Gzon());
        use(BeerController.class);
        use(TestController.class);
        use(new ApiTool()
                .swagger("/swagger")
        );
        use(new Metrics()
                .ping()
                .healthCheck("db", new Check())
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
