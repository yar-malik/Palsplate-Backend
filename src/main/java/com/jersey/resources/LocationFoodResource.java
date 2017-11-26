package com.jersey.resources;

import com.jersey.persistence.FoodDao;
import com.jersey.persistence.LocationFoodDao;
import com.jersey.representations.LocationFood;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
@Transactional
public class LocationFoodResource {

    private LocationFoodDao locationFoodDao;

    @Autowired
    private FoodDao foodDao;

    @Inject
    public LocationFoodResource(LocationFoodDao locationFoodDao) {
        this.locationFoodDao= locationFoodDao;
    }

    @Path("public/location_foods")
    @GET
    public List<LocationFood> getAll(){
        return this.locationFoodDao.findAll();
    }

    @GET
    @Path("public/location_foods/{id}")
    public LocationFood getCook(@PathParam("id")long id) {
        LocationFood locationFood = locationFoodDao.findOne(id);
        if (locationFood == null) {
            throw new WebApplicationException((Response.Status.NOT_FOUND));
        }
        return locationFood;
    }
    
    /**
     * Create new LocationFood
     * @param locationFood
     * @return new locationFood
     */
    @POST
    @Path("secure/location_foods")
    public LocationFood save(@Valid LocationFood locationFood) {
            return locationFoodDao.save(locationFood);
    }

    /**
     * Update existing LocationFood
     * @param id
     * @param locationFood
     * @return updated locationFood
     */
    @PUT
    @Path("secure/location_foods/{id}")
//    @PreAuthorize("hasPermission(#id,'CookResource', 'ROLE_USER,ROLE_ADMIN')")
    public LocationFood update(@PathParam("id")long id, @Valid LocationFood locationFood) {
        if(locationFoodDao.findOne(id) == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }else {
            locationFood.setId(id);
            return locationFoodDao.save(locationFood);
        }
    }

    /**
     * Delete locationFood
     * @param id
     */
    @DELETE
    @Path("secure/location_foods/{id}")
//    @PreAuthorize("hasPermission(#id,'CookResource', 'ROLE_USER,ROLE_ADMIN')")
    public void delete(@PathParam("id")long id) {
        LocationFood locationFood = locationFoodDao.findOne(id);
        if(locationFood == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }else {
            locationFoodDao.delete(locationFood);
        }
    }
}