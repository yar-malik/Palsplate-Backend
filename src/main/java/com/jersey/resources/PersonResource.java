package com.jersey.resources;

import com.jersey.persistence.PersonDao;
import com.jersey.representations.Person;
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
@Transactional
@Component
public class PersonResource {
    private PersonDao personDao;

    @Inject
    public PersonResource(PersonDao personDao) {
        this.personDao = personDao;
    }

    /**
     * Get all Users
     *
     * @return persons
     */
    @GET
    @Path("secure/persons")
    public List<Person> getAll() {
        List<Person> persons = this.personDao.findAll();
        return persons;
    }

    /**
     * Get single Person
     *
     * @param id
     * @return person
     */
    @GET
    @Path("secure/persons/{id}")
    public Person getOne(@PathParam("id") long id) {
        Person person = personDao.findOne(id);
        if (person == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return person;
        }
    }

    /**
     * Create new Person
     *
     * @param person
     * @return new person
     */
    @POST
    @Path("secure/persons")
    public Person save(@Valid Person person) {
        return personDao.save(person);
    }

    /**
     * Update existing Person
     *
     * @param id
     * @param person
     * @return updated person
     */
    @PUT
    @Path("secure/persons/{id}")
    public Person update(@PathParam("id") long id, @Valid Person person) {
        if (personDao.findOne(id) == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            person.setId(id);
            return personDao.save(person);
        }
    }

    /**
     * Delete person
     *
     * @param id
     */
    @DELETE
    @Path("secure/persons/{id}")
    public void delete(@PathParam("id") long id) {
        Person person = personDao.findOne(id);
        if (person == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            personDao.delete(person);
        }
    }
}