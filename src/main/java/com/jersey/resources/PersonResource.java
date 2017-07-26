package com.jersey.resources;

import com.jersey.persistence.PersonDao;
import com.jersey.representations.Person;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
   // @PreAuthorize("hasPermission('PersonResource', 'ROLE_ADMIN')")
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
    @PreAuthorize("hasPermission(#id, 'PersonResource', 'ROLE_USER,ROLE_ADMIN')")
    public Person getOne(@PathParam("id") long id) {
        Person person = personDao.findOne(id);
        if (person == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return person;
        }
    }

    @GET
    @Path("secure/currentuser")
    public Person getPersonViaAccessToken()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return CopyPersonSafe(personDao.findByEmail(email));
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
    @PreAuthorize("hasPermission(#id, 'PersonResource', 'ROLE_USER,ROLE_ADMIN')")
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
    @PreAuthorize("hasPermission(#id, 'PersonResource', 'ROLE_ADMIN')")
    public void delete(@PathParam("id") long id) {
        Person person = personDao.findOne(id);
        if (person == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            personDao.delete(person);
        }
    }

    /**
     * Copies the person object without password and other security impacting information
     * @return Copied person object
     */
    private Person CopyPersonSafe(Person person)
    {
        Person newPerson = new Person();

        newPerson.setEmail(person.getEmail());
        newPerson.setId(person.getId());
        newPerson.setFirstName(person.getFirstName());
        newPerson.setLastName(person.getLastName());
        newPerson.setAddress(person.getAddress());
        newPerson.setPhoneNumber(person.getPhoneNumber());
        newPerson.setDescription(person.getDescription());

        return  newPerson;
    }
}