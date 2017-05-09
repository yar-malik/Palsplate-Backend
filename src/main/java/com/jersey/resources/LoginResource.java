package com.jersey.resources;


import com.jersey.persistence.LoginDao;
import com.jersey.representations.Login;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
@Transactional
public class LoginResource {
    private final LoginDao loginDao;

    @Inject
    public LoginResource(LoginDao loginDao) {
        this.loginDao = loginDao;
    }

    @GET
    public List<Login> getAll(){
        return this.loginDao.findAll();
    }

    @GET
    @Path("{id}/login")
    public Login getAllProductsForMember(@PathParam("id")long id) {
        Login login = loginDao.findOne(id);
        if (login == null) {
            throw new WebApplicationException((Response.Status.NOT_FOUND));
        }

        //Poke products
//        login.getProducts().size();
        return login;
    }

    @GET
    @Path("{id}")
    public Login getLogin(@PathParam("id")long id) {
        Login login = loginDao.findOne(id);
        if (login == null) {
            throw new WebApplicationException((Response.Status.NOT_FOUND));
        }
        return login;
    }
}