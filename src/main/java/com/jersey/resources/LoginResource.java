package com.jersey.resources;

import com.jersey.persistence.LoginDao;
import com.jersey.representations.Login;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/logins")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@Component
public class LoginResource {
    private LoginDao loginDao;

    @Inject
    public LoginResource(LoginDao loginDao) {
        this.loginDao = loginDao;
    }

    /**
     * Get all Users
     *
     * @return logins
     */
    @GET
    public List<Login> getAll() {
        List<Login> logins = this.loginDao.findAll();
        return logins;
    }

    /**
     * Get single Login
     *
     * @param id
     * @return login
     */
    @GET
    @Path("{id}")
    public Login getOne(@PathParam("id") long id) {
        Login login = loginDao.findOne(id);
        if (login == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return login;
        }
    }

    /**
     * Create new Login
     *
     * @param login
     * @return new login
     */
    @POST
    public Login save(@Valid Login login) {
        return loginDao.save(login);
    }

    /**
     * Update existing Login
     *
     * @param id
     * @param login
     * @return updated login
     */
    @PUT
    @Path("{id}")
    public Login update(@PathParam("id") long id, @Valid Login login) {
        if (loginDao.findOne(id) == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            login.setId(id);
            return loginDao.save(login);
        }
    }

    /**
     * Delete login
     *
     * @param id
     */
    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") long id) {
        Login login = loginDao.findOne(id);
        if (login == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            loginDao.delete(login);
        }
    }
}