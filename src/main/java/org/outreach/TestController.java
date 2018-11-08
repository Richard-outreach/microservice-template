package org.outreach;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.*;
import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.extern.log4j.Log4j2;
import org.jooby.Mutant;
import org.jooby.Request;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.GET;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Consumes("application/json")
@Produces("application/json")
public class TestController {

    private final AmazonCloudWatch cloudWatch;

    public TestController() {
        this.cloudWatch = AmazonCloudWatchClientBuilder.standard().build();
    }

    @GET
    @Path("/env")
    public Map<String, String> getEnv() {
        return System.getenv();
    }

    @GET
    @Path("/test")
    public Map<String, Object> getTestData(Request request) {
        Map<String, Object> result = new HashMap<>();

        Map<String, String> headerMap = new HashMap<>();
        for (String header : request.headers().keySet()) {
            Mutant value = request.headers().get(header);
            headerMap.put(header, value.value("[failed_to_convert_to_string]"));
        }
        result.put("headers", headerMap);
        Map<String, String> paramMap = new HashMap<>();
        for (String paramKey : request.params().toMap().keySet()) {
            Mutant value = request.params().toMap().get(paramKey);
            paramMap.put(paramKey, value.toString());
        }

        result.put("queryParams", paramMap);
        return result;
    }

    @GET
    @Path("/metric")
    public void postMetric(Request req) {

        MetricDatum datum = new MetricDatum()
                .withMetricName("TestMetric")
                .withUnit(StandardUnit.None)
                .withValue(1.0);

        PutMetricDataRequest request = new PutMetricDataRequest()
                .withNamespace("TestServiceAppMetrics")
                .withMetricData(datum);

        PutMetricDataResult response = cloudWatch.putMetricData(request);
        log.debug("id: " + response.getSdkResponseMetadata().getRequestId());
    }
}
