package com.jersey.resources;

import com.jersey.persistence.UserDao;
import com.jersey.representations.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@Component
public class UserResource {
    private UserDao userDao;

    @Inject
    public UserResource(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Get all Users
     *
     * @return users
     */
    @GET
    public List<User> getAll() {
        List<User> users = this.userDao.findAll();
        return users;
    }

    /**
     * Get single User
     *
     * @param id
     * @return user
     */
    @GET
    @Path("{id}")
    public User getOne(@PathParam("id") long id) {
        User user = userDao.findOne(id);
        if (user == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return user;
        }
    }

    /**
     * Create new User
     *
     * @param user
     * @return new user
     */
    @POST
    public User save(@Valid User user) {
        return userDao.save(user);
    }

    /**
     * Update existing User
     *
     * @param id
     * @param user
     * @return updated user
     */
    @PUT
    @Path("{id}")
    public User update(@PathParam("id") long id, @Valid User user) {
        if (userDao.findOne(id) == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            user.setId(id);
            return userDao.save(user);
        }
    }

    /**
     * Delete user
     *
     * @param id
     */
    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") long id) {
        User user = userDao.findOne(id);
        if (user == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            userDao.delete(user);
        }
    }
}