package com.jersey.resources;

import com.jersey.persistence.FoodDao;
import com.jersey.representations.Food;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/foods")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@Component
public class FoodResource {
    private FoodDao foodDao;
    @Inject
    public FoodResource(FoodDao foodDao){
        this.foodDao = foodDao;
    }

    /**
     * Get all Foods
     * @return foods
     */
    @GET
    public List<Food> getAll(){
        List<Food> foods = this.foodDao.findAll();
        return foods;
    }

    /**
     * Get single Food
     * @param id
     * @return food
     */
    @GET
    @Path("{id}")
    public Food getOne(@PathParam("id")long id) {
        Food food = foodDao.findOne(id);
        if(food == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }else {
            return food;
        }
    }

    /**
     * Create new Food
     * @param food
     * @return new food
     */
    @POST
    public Food save(@Valid Food food) {
        return foodDao.save(food);
    }

    /**
     * Update existing Food
     * @param id
     * @param food
     * @return updated food
     */
    @PUT
    @Path("{id}")
    public Food update(@PathParam("id")long id, @Valid Food food) {
        if(foodDao.findOne(id) == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }else {
            food.setId(id);
            return foodDao.save(food);
        }
    }

    /**
     * Delete food
     * @param id
     */
    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id")long id) {
        Food food = foodDao.findOne(id);
        if(food == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }else {
            foodDao.delete(food);
        }
    }
}