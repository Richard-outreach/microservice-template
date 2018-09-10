package org.outreach;

import org.jooby.mvc.*;

import javax.inject.Named;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Consumes("application/json")
@Produces("application/json")
@Path("/beers")
public class BeerController {

    private static final ConcurrentHashMap<String, Beer> BEER_MAP = new ConcurrentHashMap<>();

    @GET
    @Path(value = "/env")
    public Map<String, String> getEnv() {
        return System.getenv();
    }

    /**
     * Gets a beer
     * @param id the id of the beer
     * @return the beer
     */
    @GET
    @Path(value = "/{id}")
    public Beer getBeer(@Named("id") String id) {
        return BEER_MAP.get(id);
    }

    /**
     * Gets all beers
     * @return a collection of beers
     */
    @GET
    public Collection<Beer> getAllBeers() {
        return BEER_MAP.values();
    }

    /**
     * Creates a new beer
     * @param beer the beer to create
     * @return the created beer
     */
    @POST
    public Beer createBeer(@Body Beer beer) {
        String id = UUID.randomUUID().toString().replace("-", "");
        beer.setId(id);
        BEER_MAP.put(id, beer);
        return beer;
    }

    /**
     * Updates a beer
     * @param id the id of the beer to update
     * @param beer the beer to update
     * @return the updated beer
     */
    @PUT
    @Path(value = "/{id}")
    public Beer updateBeer(@Named("id") String id, @Body Beer beer) {
        BEER_MAP.put(id, beer);
        return beer;
    }

    /**
     * Deletes a beer
     * @param id the id of the beer to delete
     */
    @DELETE
    @Path(value = "/{id}")
    public void deleteBeer(@Named("id") String id) {
        BEER_MAP.remove(id);
    }

}
