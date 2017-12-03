package com.jersey.resources;

import com.jersey.persistence.LocationPersonDao;
import com.jersey.persistence.PersonDao;
import com.jersey.representations.LocationPerson;
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
public class LocationPersonResource {

    private LocationPersonDao locationPersonDao;

    @Autowired
    private PersonDao personDao;

    @Inject
    public LocationPersonResource(LocationPersonDao locationPersonDao) {
        this.locationPersonDao= locationPersonDao;
    }

    @Path("public/location_persons")
    @GET
    public List<LocationPerson> getAll(){
        return this.locationPersonDao.findAll();
    }

    @GET
    @Path("public/location_persons/{id}")
    public LocationPerson getCook(@PathParam("id")long id) {
        LocationPerson locationPerson = locationPersonDao.findOne(id);
        if (locationPerson == null) {
            throw new WebApplicationException((Response.Status.NOT_FOUND));
        }
        return locationPerson;
    }
    
    /**
     * Create new LocationPerson
     * @param locationPerson
     * @return new locationPerson
     */
    @POST
    @Path("public/location_persons")
    public LocationPerson save(@Valid LocationPerson locationPerson) {
            return locationPersonDao.save(locationPerson);
    }

    /**
     * Update existing LocationPerson
     * @param id
     * @param locationPerson
     * @return updated locationPerson
     */
    @PUT
    @Path("secure/location_persons/{id}")
//    @PreAuthorize("hasPermission(#id,'CookResource', 'ROLE_USER,ROLE_ADMIN')")
    public LocationPerson update(@PathParam("id")long id, @Valid LocationPerson locationPerson) {
        if(locationPersonDao.findOne(id) == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }else {
            locationPerson.setId(id);
            return locationPersonDao.save(locationPerson);
        }
    }

    /**
     * Delete locationPerson
     * @param id
     */
    @DELETE
    @Path("secure/location_persons/{id}")
//    @PreAuthorize("hasPermission(#id,'CookResource', 'ROLE_USER,ROLE_ADMIN')")
    public void delete(@PathParam("id")long id) {
        LocationPerson locationPerson = locationPersonDao.findOne(id);
        if(locationPerson == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }else {
            locationPersonDao.delete(locationPerson);
        }
    }
}