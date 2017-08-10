package com.jersey.resources;

import com.jersey.persistence.*;
import com.jersey.representations.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class CustomerResource {

    private CustomerDao customerDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private CookDao cookDao;

    @Autowired
    private FoodDao foodDao;

    @Autowired
    private ReservationDao reservationDao;

    @Inject
    public CustomerResource(CustomerDao customerDao){
        this.customerDao = customerDao;
    }

    /**
     * Get all Customers
     * @return customers
     */

    @GET
    @Path("secure/customers")
    @PreAuthorize("hasPermission('CustomerResource', 'ROLE_ADMIN')")
    public List<Customer> getAll(){
        List<Customer> customers = this.customerDao.findAll();
        return customers;
    }

    /**
     * Get single Customer
     * @param id
     * @return customer
     */
    @GET
    @Path("secure/customers/{id}")
    @PreAuthorize("hasPermission(#id,'CustomerResource', 'ROLE_USER,ROLE_ADMIN')")
    public Customer getOne(@PathParam("id")long id) {
        Customer customer = customerDao.findOne(id);
        if(customer == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }else {
            return customer;
        }
    }

    @GET
    @Path("secure/customers/{id}/reservations")
    public JSONArray getAllReservationForForCustomer(@PathParam("id")long id) {

        Customer customer = customerDao.findOne(id);
        JSONObject reservationJsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        if (customer == null) {
            throw new WebApplicationException((Response.Status.NOT_FOUND));
        }
        else{

            for(Reservation reservation: customer.getReservations()){
                JSONObject jsonReservationAll = new JSONObject();
                Food food = foodDao.findOne(reservation.getFoodId());
                Cook cook = cookDao.findOne(food.getCook_id());
                Person person = personDao.findOne(cook.getPerson_id());
                jsonReservationAll.put("cookFirstName", person.getFirstName());
                jsonReservationAll.put("cookLastName", person.getLastName());
                jsonReservationAll.put("cookPhoto", person.getPhotoPublicId());
                jsonReservationAll.put("food", food);
                jsonArray.add(jsonReservationAll);
            }
        }
        return jsonArray;
    }

    @GET
    @Path("secure/customers/{id}/photo")
    public JSONObject getPhotoForCustomer(@PathParam("id")long id) {
        Customer customer = customerDao.findOne(id);
        if (customer == null) {
            throw new WebApplicationException((Response.Status.NOT_FOUND));
        }
        else{
            Person person = personDao.findOne(customer.getPerson_id());
            JSONObject jsonPhoto = new JSONObject();
            jsonPhoto.put("customerPhoto", person.getPhotoPublicId());
            return jsonPhoto;
        }
    }


    /**
     * Create new Customer
     * @param customer
     * @return new customer
     */
    @POST
    @Path("secure/customers")
    public Customer save(@Valid Customer customer) {
        return customerDao.save(customer);
    }

    /**
     * Update existing Customer
     * @param id
     * @param customer
     * @return updated customer
     */

    @PUT
    @Path("secure/customers/{id}")
    @PreAuthorize("hasPermission(#id,'CustomerResource', 'ROLE_USER,ROLE_ADMIN')")
    public Customer update(@PathParam("id")long id, @Valid Customer customer) {
        if(customerDao.findOne(id) == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }else {
            customer.setId(id);
            return customerDao.save(customer);
        }
    }

    /**
     * Delete customer
     * @param id
     */
    @DELETE
    @Path("secure/customers/{id}")
    @PreAuthorize("hasPermission(#id,'CustomerResource', 'ROLE_USER,ROLE_ADMIN')")
    public void delete(@PathParam("id")long id) {
        Customer customer = customerDao.findOne(id);
        if(customer == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }else {
            customerDao.delete(customer);
        }
    }
}