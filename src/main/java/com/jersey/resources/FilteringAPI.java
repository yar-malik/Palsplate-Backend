package com.jersey.resources;

import com.jersey.persistence.FoodDao;
import com.jersey.representations.Food;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class FilteringAPI {

    private static final Logger log = LogManager.getLogger(Filter.class);

    @Inject
    private FoodDao foodDao;

    @GET
    @Path("public/filters/distance/maxDist={maxDist}/lon={lon}/lat={lat}")
    public List<Food> getAllImagesForFood(@PathParam("maxDist")double maxDist, @PathParam("lon")double lon, @PathParam("lat")double lat) {

        List<Food> foods = this.foodDao.findAll();
        List<Food> filterFoods = new ArrayList<>();
        Double disDiff;

        for(Food f: foods){
            disDiff = distFrom(lon, lat, f.getLon(), f.getLat())/1000;
            System.out.println(disDiff);
            if(disDiff < maxDist){
                filterFoods.add(f);
            }
        }

        log.info("Filtered FoodList Size: " + filterFoods.size());

        return filterFoods;

    }

    public double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist;
    }


    @GET
    @Path("public/filters/price/max={max}")
    public List<Food> getAllFoodsWithPriceLowerThan(@PathParam("max")long max) {
        List<Food> foods = this.foodDao.findAll();
        List<Food> filterFoods = new ArrayList<>();

        for(Food f: foods){
            if(f.getPrice() <= max){
                filterFoods.add(f);
            }
        }

        log.info("Filtered FoodList Size: " + filterFoods.size());

        return filterFoods;
    }

    @GET
    @Path("public/filters/foodtype/type={type}")
    public List<Food> getAllFoodsWithFoodType(@PathParam("type")String type) {
        List<Food> foods = this.foodDao.findAll();
        List<Food> filterFoods = new ArrayList<>();

        for(Food f: foods){
            if(f.getFood_type() .equalsIgnoreCase(type) ){
                filterFoods.add(f);
            }
        }

        log.info("Filtered FoodList  Size: " + filterFoods.size());

        return filterFoods;
    }

    @GET
    @Path("public/filters/cuisinetype/type={type}")
    public List<Food> getAllFoodsWithCuisineType(@PathParam("type")String type) {
        List<Food> foods = this.foodDao.findAll();
        List<Food> filterFoods = new ArrayList<>();

        for(Food f: foods){
            if(f.getCuisine_type() .equalsIgnoreCase(type) ){
                filterFoods.add(f);
            }
        }

        log.info("Filtered FoodList  Size: " + filterFoods.size());

        return filterFoods;
    }


}
