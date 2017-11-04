package com.jersey.resources;

import com.jersey.persistence.CookDao;
import com.jersey.persistence.PersonDao;
import com.jersey.representations.Cook;
import com.jersey.representations.Customer;
import com.jersey.representations.Person;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

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
public class CookResource {
    private final CookDao cookDao;

    @Autowired
    private PersonDao personDao;

    @Inject
    public CookResource(CookDao cookDao) {
        this.cookDao = cookDao;
    }

    @Path("public/cooks")
    @GET
    public List<Cook> getAll(){
        return this.cookDao.findAll();
    }

    @GET
    @Path("public/cooks/{id}/foods")
    public Cook getAllFoodsForCook(@PathParam("id")long id) {
        Cook cook = cookDao.findOne(id);
        if (cook == null) {
            throw new WebApplicationException((Response.Status.NOT_FOUND));
        }

        cook.getFoods().size();
        return cook;
    }

    @GET
    @Path("public/cooks/{id}")
    public Cook getCook(@PathParam("id")long id) {
        Cook cook = cookDao.findOne(id);
        if (cook == null) {
            throw new WebApplicationException((Response.Status.NOT_FOUND));
        }
        return cook;
    }

    @GET
    @Path("public/cooks/{id}/photo")
    public JSONObject getPhotoForCook(@PathParam("id")long id) {
        Cook cook = cookDao.findOne(id);
        if (cook == null) {
            throw new WebApplicationException((Response.Status.NOT_FOUND));
        }
        else{
            Person person = personDao.findOne(cook.getPerson_id());
            JSONObject jsonPhoto = new JSONObject();
            jsonPhoto.put("cookPhoto", person.getPhotoPublicId());
            return jsonPhoto;
        }
    }

    /**
     * Create new Cook
     * @param cook
     * @return new cook
     */
    @POST
    @Path("secure/cooks")
    public Cook save(@Valid Cook cook) {
        return cookDao.save(cook);
    }

    /**
     * Update existing Cook
     * @param id
     * @param cook
     * @return updated cook
     */
    @PUT
    @Path("secure/cooks/{id}")
//    @PreAuthorize("hasPermission(#id,'CookResource', 'ROLE_USER,ROLE_ADMIN')")
    public Cook update(@PathParam("id")long id, @Valid Cook cook) {
        if(cookDao.findOne(id) == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }else {
            cook.setId(id);
            return cookDao.save(cook);
        }
    }

    /**
     * Delete cook
     * @param id
     */
    @DELETE
    @Path("secure/cooks/{id}")
//    @PreAuthorize("hasPermission(#id,'CookResource', 'ROLE_USER,ROLE_ADMIN')")
    public void delete(@PathParam("id")long id) {
        Cook cook = cookDao.findOne(id);
        if(cook == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }else {
            cookDao.delete(cook);
        }
    }
}