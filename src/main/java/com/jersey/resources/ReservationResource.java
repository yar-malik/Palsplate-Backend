package com.jersey.resources;

import com.jersey.persistence.ReservationDao;
import com.jersey.representations.Customer;
import com.jersey.representations.Reservation;
import com.jersey.representations.Reservation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
@Transactional
public class ReservationResource {
    private final ReservationDao reservationDao;

    @Inject
    public ReservationResource(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    @Path("public/reservations")
    @GET
    public List<Reservation> getAll(@QueryParam("page") Integer page,
                                    @QueryParam("size") Integer size,
                                    @QueryParam("sort") List<String> sort) {

        if(page == null && size == null)
        {
            return this.reservationDao.findAll();
        }

        //set default value for page
        if(page == null)
        {
            page = new Integer(0);
        }

        //set defaullt value for size
        if(size == null)
        {
            size = new Integer(3);
        }

        List<Sort.Order> orders = new ArrayList<>();

        for (String propOrder: sort) {

            String[] propOrderSplit = propOrder.split(",");
            String property = propOrderSplit[0];

            if (propOrderSplit.length == 1) {
                orders.add(new Sort.Order(property));
            } else {
                Sort.Direction direction
                        = Sort.Direction.fromStringOrNull(propOrderSplit[1]);
                orders.add(new Sort.Order(direction, property));
            }
        }

        Pageable pageable = new PageRequest(page, size, orders.isEmpty() ? null : new Sort(orders));

        return  this.reservationDao.findAll(pageable).getContent();
    }


    @GET
    @Path("public/reservations/{id}")
    public Reservation getReservation(@PathParam("id")long id) {
        Reservation reservation = reservationDao.findOne(id);
        if (reservation == null) {
            throw new WebApplicationException((Response.Status.NOT_FOUND));
        }
        return reservation;
    }

    /**
     * Create new Reservation
     * @param reservation
     * @return new reservation
     */
    @POST
    @Path("secure/reservations")
    public Reservation save(@Valid Reservation reservation) {
        return reservationDao.save(reservation);
    }

    /**
     * Create new Reservation
     * @param id, active
     * @return new reservation
     */
    @POST
    @Path("secure/reservations/{id}")
    public Reservation updateIsActive(@PathParam("id")long id, @QueryParam("active")boolean active) {

        Reservation reservation = reservationDao.findOne(id);
        if (reservation == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        else {
            reservation.setIs_active(active);
            return reservationDao.save(reservation);
        }
    }


    /**
     * Update existing Reservation
     * @param id
     * @param reservation
     * @return updated reservation
     */
    @PUT
    @Path("secure/reservations/{id}")
//    @PreAuthorize("hasPermission(#id,'ReservationResource', 'ROLE_USER,ROLE_ADMIN')")
    public Reservation update(@PathParam("id")long id, @Valid Reservation reservation) {
        if(reservationDao.findOne(id) == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }else {
            reservation.setId(id);
            return reservationDao.save(reservation);
        }
    }

    /**
     * Delete reservation
     * @param id
     */
    @DELETE
    @Path("secure/reservations/{id}")
//    @PreAuthorize("hasPermission(#id,'ReservationResource', 'ROLE_USER,ROLE_ADMIN')")
    public void delete(@PathParam("id")long id) {
        Reservation reservation = reservationDao.findOne(id);
        if(reservation == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }else {
            reservationDao.delete(reservation);
        }
    }
}