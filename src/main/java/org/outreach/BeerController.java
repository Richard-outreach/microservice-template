package org.outreach;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.jooby.mvc.*;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@Consumes("application/json")
@Produces("application/json")
@Path("/beers")
public class BeerController {

    private DynamoDBMapper mapper;

    public BeerController() {
        AmazonDynamoDB db = AmazonDynamoDBClientBuilder.standard().enableEndpointDiscovery().build();
        this.mapper = new DynamoDBMapper(db);
    }

    /**
     * Gets a beer
     * @param id the id of the beer
     * @return the beer
     */
    @GET
    @Path(value = "/{id}")
    public Beer getBeer(@Named("id") String id) {
        return mapper.load(Beer.class, id);
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
        mapper.save(beer);
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
        Beer loaded = mapper.load(Beer.class, id);
        beer.setVersion(loaded.getVersion());
        mapper.save(beer);
        return beer;
    }

    /**
     * Deletes a beer
     * @param id the id of the beer to delete
     */
    @DELETE
    @Path(value = "/{id}")
    public void deleteBeer(@Named("id") String id) {
        Beer beer = mapper.load(Beer.class, id);
        if(beer != null) {
            mapper.delete(beer);
        }
    }

}
