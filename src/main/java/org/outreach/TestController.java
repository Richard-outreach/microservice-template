package org.outreach;

import org.jooby.Mutant;
import org.jooby.Request;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.GET;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

import java.util.HashMap;
import java.util.Map;

@Consumes("application/json")
@Produces("application/json")
public class TestController {

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
}
