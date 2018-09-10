package org.outreach;

import org.jooby.mvc.Consumes;
import org.jooby.mvc.GET;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

import java.util.Map;

@Consumes("application/json")
@Produces("application/json")
public class TestController {

    @GET
    @Path("/env")
    public Map<String, String> getEnv() {
        return System.getenv();
    }
}
