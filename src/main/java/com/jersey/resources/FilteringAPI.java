package com.jersey.resources;

import com.jersey.persistence.FoodDao;
import com.jersey.representations.Food;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
    @Path("public/filters")
    public List<Food> getUsers(
            @QueryParam("maxDist") Integer maxDist,
            @QueryParam("lon") Integer lon,
            @QueryParam("lat") Integer lat,
            @QueryParam("maxPrice") Integer maxPrice,
            @QueryParam("foodType") String foodType,
            @QueryParam("cuisineType") String cuisineType) {

        System.out.println("entering the new filter");
        List<Food> foods = this.foodDao.findAll();

        ArrayList<Food> filterByDistanceFoods = new ArrayList<>();
        ArrayList<Food> filterByMaxPriceFoods = new ArrayList<>();
        ArrayList<Food> filterByFoodTypeFoods = new ArrayList<>();
        ArrayList<Food> filterByCuisineTypeFoods= new ArrayList<>();

        if(maxDist != null && lon != null && lat != null){
            Double disDiff;

            for(Food f: foods){
                disDiff = distFrom(lon, lat, f.getLon(), f.getLat())/1000;
                System.out.println(disDiff);
                if(disDiff < maxDist){
                    filterByDistanceFoods.add(f);
                }
            }
            System.out.println(filterByDistanceFoods.size());
            foods.removeAll(filterByDistanceFoods);
        }


        if(maxPrice != null){
            for(Food f: foods){
                if(f.getPrice() <= maxPrice){
                    filterByMaxPriceFoods.add(f);
                }
            }
            System.out.println(filterByMaxPriceFoods.size());
            foods.removeAll(filterByMaxPriceFoods);
        }


        if(foodType != null){
            for(Food f: foods){
                if(f.getFood_type() .equalsIgnoreCase(foodType) ){
                    filterByFoodTypeFoods.add(f);
                }
            }
            System.out.println(filterByFoodTypeFoods.size());
            foods.removeAll(filterByFoodTypeFoods);
        }

        if(cuisineType != null){
            for(Food f: foods){
                if(f.getCuisine_type() .equalsIgnoreCase(cuisineType) ){
                    filterByCuisineTypeFoods.add(f);
                }
            }
            System.out.println(filterByCuisineTypeFoods.size());
            foods.removeAll(filterByCuisineTypeFoods);
        }
        
        return foods;
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


//    @GET
//    @Path("public/filters/distance/maxDist={maxDist}/lon={lon}/lat={lat}")
//    public ArrayList<Food> getAllImagesForFood(@PathParam("maxDist")double maxDist, @PathParam("lon")double lon, @PathParam("lat")double lat) {
//
//        List<Food> foods = this.foodDao.findAll();
//        ArrayList<Food> filterFoods = new ArrayList<>();
//        Double disDiff;
//
//        for(Food f: foods){
//            disDiff = distFrom(lon, lat, f.getLon(), f.getLat())/1000;
//            System.out.println(disDiff);
//            if(disDiff < maxDist){
//                filterFoods.add(f);
//            }
//        }
//
//        log.info("Filtered FoodList Size: " + filterFoods.size());
//
//        return filterFoods;
//
//    }
//
//
//    @GET
//    @Path("public/filters/price/max={max}")
//    public ArrayList<Food> getAllFoodsWithPriceLowerThan(@PathParam("max")long max) {
//        List<Food> foods = this.foodDao.findAll();
//        ArrayList<Food> filterFoods = new ArrayList<>();
//
//        for(Food f: foods){
//            if(f.getPrice() <= max){
//                filterFoods.add(f);
//            }
//        }
//
//        log.info("Filtered FoodList Size: " + filterFoods.size());
//
//        return filterFoods;
//    }
//
//    @GET
//    @Path("public/filters/foodtype/type={type}")
//    public ArrayList<Food> getAllFoodsWithFoodType(@PathParam("type")String type) {
//        List<Food> foods = this.foodDao.findAll();
//        ArrayList<Food> filterFoods = new ArrayList<>();
//
//        for(Food f: foods){
//            if(f.getFood_type() .equalsIgnoreCase(type) ){
//                filterFoods.add(f);
//            }
//        }
//
//        log.info("Filtered FoodList  Size: " + filterFoods.size());
//
//        return filterFoods;
//    }
//
//    @GET
//    @Path("public/filters/cuisinetype/type={type}")
//    public ArrayList<Food> getAllFoodsWithCuisineType(@PathParam("type")String type) {
//        List<Food> foods = this.foodDao.findAll();
//        ArrayList<Food> filterFoods = new ArrayList<>();
//
//        for(Food f: foods){
//            if(f.getCuisine_type() .equalsIgnoreCase(type) ){
//                filterFoods.add(f);
//            }
//        }
//
//        log.info("Filtered FoodList  Size: " + filterFoods.size());
//
//        return filterFoods;
//    }
}
