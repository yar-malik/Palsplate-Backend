package com.jersey.resources;

import com.jersey.persistence.CookDao;
import com.jersey.representations.Cook;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/cooks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@Component
public class CookResource {
    private CookDao cookDao;

    @Inject
    public CookResource(CookDao cookDao) {
        this.cookDao = cookDao;
    }

    /**
     * Get all Cooks
     *
     * @return cooks
     */
    @GET
    public List<Cook> getAll() {
        List<Cook> cooks = this.cookDao.findAll();
        return cooks;
    }

    /**
     * Get single Cook
     *
     * @param id
     * @return cook
     */
    @GET
    @Path("{id}")
    public Cook getOne(@PathParam("id") long id) {
        Cook cook = cookDao.findOne(id);
        if (cook == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return cook;
        }
    }

    /**
     * Create new Cook
     *
     * @param cook
     * @return new cook
     */
    @POST
    public Cook save(@Valid Cook cook) {
        return cookDao.save(cook);
    }

    /**
     * Update existing Cook
     *
     * @param id
     * @param cook
     * @return updated cook
     */
    @PUT
    @Path("{id}")
    public Cook update(@PathParam("id") long id, @Valid Cook cook) {
        if (cookDao.findOne(id) == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            cook.setId(id);
            return cookDao.save(cook);
        }
    }

    /**
     * Delete cook
     *
     * @param id
     */
    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") long id) {
        Cook cook = cookDao.findOne(id);
        if (cook == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            cookDao.delete(cook);
        }
    }
}