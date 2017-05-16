package com.jersey.resources;

import com.jersey.persistence.CookDao;
import com.jersey.representations.Cook;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/cooks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
@Transactional
public class CookResource {
    private final CookDao cookDao;

    @Inject
    public CookResource(CookDao cookDao) {
        this.cookDao = cookDao;
    }
    @GET
    public List<Cook> getAll(){
        return this.cookDao.findAll();
    }

    @GET
    @Path("{id}/foods")
    public Cook getAllFoodsForCook(@PathParam("id")long id) {
        Cook cook = cookDao.findOne(id);
        if (cook == null) {
            throw new WebApplicationException((Response.Status.NOT_FOUND));
        }

        cook.getFoods().size();
        return cook;
    }

    @GET
    @Path("{id}")
    public Cook getCook(@PathParam("id")long id) {
        Cook cook = cookDao.findOne(id);
        if (cook == null) {
            throw new WebApplicationException((Response.Status.NOT_FOUND));
        }
        return cook;
    }
}