package com.jersey.resources;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.jersey.persistence.*;
import com.jersey.representations.*;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.stream.Location;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@Component
public class FoodResource {

    private static final Logger log = LogManager.getLogger(FoodResource.class);

    @Autowired
    private Cloudinary cloudinary;

    private FoodDao foodDao;

    @Autowired
    private LocationFoodDao locationFoodDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private CookDao cookDao;

    @Inject
    public FoodResource(FoodDao foodDao){
        this.foodDao = foodDao;
    }

    @Inject
    private ImageDao imageDao;

    /**
     * Get single Food
     * @param id
     * @return food
     */
    @GET
    @Path("public/foods/{id}")
    public Food getOne(@PathParam("id")long id) {
        Food food = foodDao.findOne(id);
        if(food == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }else {
            return food;
        }
    }

    /**
     * Get all images for specific food
     * @param id
     * @return food
     */
    @GET
    @Path("public/foods/{id}/images")
    public Food getAllImagesForFood(@PathParam("id")long id) {
        Food food = foodDao.findOne(id);
        if (food == null) {
            throw new WebApplicationException((Response.Status.NOT_FOUND));
        }

        food.getImages().size();
        return food;
    }

    /**
     * Get all reviews for specific food
     * @param id
     * @return food
     */
    @GET
    @Path("public/foods/{id}/reviews")
    public Food getAllReviewsForFood(@PathParam("id")long id) {
        Food food = foodDao.findOne(id);
        if (food == null) {
            throw new WebApplicationException((Response.Status.NOT_FOUND));
        }
        food.getReviews().size();
        return food;
    }

    /**
     * Get location for specific food
     * @param id
     * @return food
     */
    @GET
    @Path("secure/foods/{id}/location_foods")
    public Food getLocationForFood(@PathParam("id")long id) {
        Food food = foodDao.findOne(id);
        if (food  == null) {
            throw new WebApplicationException((Response.Status.NOT_FOUND));
        }
        food.getLocationFood().size();
        return food;
    }

    /**
     * Get food object with cooks information
     * @param id
     * @return JSONObject
     */
    @GET
    @Path("public/foods/{id}/cookinfo")
    public JSONObject getCookInfoForFood(@PathParam("id")long id) {
        Food food = foodDao.findOne(id);
        if (food  == null) {
            throw new WebApplicationException((Response.Status.NOT_FOUND));
        }

        Cook cook = cookDao.findOne(food.getCook_id());
        Person person = personDao.findOne(cook.getPerson_id());

        JSONObject foodcookinfo = new JSONObject();
        foodcookinfo.put("food", food);
        foodcookinfo.put("firstname", person.getFirstName());
        foodcookinfo.put("lastname", person.getLastName());
        foodcookinfo.put("description", person.getDescription());
        foodcookinfo.put("photo_id", person.getPhotoPublicId());
        foodcookinfo.put("food_average_rating", calculateAverageRating(food.getReviews()));

        return foodcookinfo;
    }

    public double calculateAverageRating(Set<Review> reviewSet){

        double averageRating = 0;

        if(reviewSet.size() > 0){
            double totalRatingPoints = 0;

            for(Review review: reviewSet){
                totalRatingPoints += review.getRating();
            }
            averageRating = totalRatingPoints/reviewSet.size();
        }

        return averageRating;
    }

    /**
     * Get food object with cooks information
     * @param
     * @return JSONObject
     */
    @GET
    @Path("public/customs/foodsWithCooks")
    public JSONObject getFoodsWithCooks(@QueryParam("page") Integer page,
                                  @QueryParam("size") Integer size,
                                  @QueryParam("sort") List<String> sort) {

        List<Food> foods = null;

        if(page == null && size == null){
            foods =  this.foodDao.findAll();
        }
        else {
            if (page == null) {
                page = new Integer(0);
            }
            if (size == null) {
                size = new Integer(3);
            }
            List<Sort.Order> orders = new ArrayList<>();

            for (String propOrder : sort) {

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

            foods = this.foodDao.findAll(pageable).getContent();
        }

        JSONObject foodCookCompleteObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for(Food food: foods){

            Cook cook = cookDao.findOne(food.getCook_id());
            Person person = personDao.findOne(cook.getPerson_id());

            JSONObject foodcookinfo = new JSONObject();
            foodcookinfo.put("id", food.getId());
            foodcookinfo.put("cook_name", person.getFirstName() + " " + person.getLastName());
            foodcookinfo.put("cook_photo", person.getPhotoPublicId());
            foodcookinfo.put("cook_description", person.getDescription());
            foodcookinfo.put("food_average_rating", calculateAverageRating(food.getReviews()));
            jsonArray.add(foodcookinfo);

        }

        foodCookCompleteObject.put("company", "Palsplate");
        foodCookCompleteObject.put("foodsWithCooksJson", jsonArray);

        return foodCookCompleteObject;
    }

    /**
     * Create new Food
     * @param food
     * @return new food
     */
    @POST
    @Path("secure/foods")
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
    @Path("secure/foods/{id}")
//    @PreAuthorize("hasPermission(#id,'FoodResource', 'ROLE_USER,ROLE_ADMIN')")
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
    @Path("secure/foods/{id}")
//    @PreAuthorize("hasPermission(#id,'FoodResource', 'ROLE_USER,ROLE_ADMIN')")
    public void delete(@PathParam("id")long id) {
        Food food = foodDao.findOne(id);
        if(food == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }else {
            foodDao.delete(food);
        }
    }

    /**
     * Create new Image
     * @param uploadedInputStream
     * @param fileDetail
     * @param food_id
     * @return new Image for a specific food_id
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("public/foods/{food_id}/images")
    public Image uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @PathParam("food_id")long food_id) throws IOException {

        log.info("fileDetail: " + fileDetail);
        log.info("fileDetail.getName: " + fileDetail.getName());
        log.info("fileDetail.getFileName: " + fileDetail.getFileName());

        File myfile = inputStream2file(uploadedInputStream, fileDetail.getFileName(), fileDetail.getType());
        Map uploadResult = cloudinary.uploader().upload(myfile, ObjectUtils.emptyMap());

        log.info("cloudinary secure_url: " + uploadResult.get("secure_url"));
        log.info("cloudinary public_id: " + uploadResult.get("public_id"));
        log.info("cloudinary original_filename: " + uploadResult.get("original_filename"));

        Image image = new Image();
        image.setFilename(fileDetail.getFileName());
        image.setFood_id(Long.valueOf(food_id));
        image.setCloudinary_public_id(uploadResult.get("public_id").toString());

        return imageDao.save(image);
    }

    /**
     * Get Foods
     * @return foods
     */
    @GET
    @Path("public/foods")
    public List<Food> getUsers(
            @QueryParam("maxDist") Double maxDist,
            @QueryParam("lon") Double lon,
            @QueryParam("lat") Double lat,
            @QueryParam("maxPrice") Double maxPrice,
            @QueryParam("foodType") String foodType,
            @QueryParam("cuisineType") String cuisineType,
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size,
            @QueryParam("sort") List<String> sort) {

        List<Food> foods = null;

        if(page == null && size == null){
            foods =  this.foodDao.findAll();
        }
        else{
            if(page == null){
                page = new Integer(0);
            }
            if(size == null){
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

            foods = this.foodDao.findAll(pageable).getContent();

        }

        ArrayList<Food> filterByDistanceFoods = new ArrayList<>();
        ArrayList<Food> filterByMaxPriceFoods = new ArrayList<>();
        ArrayList<Food> filterByFoodTypeFoods = new ArrayList<>();
        ArrayList<Food> filterByCuisineTypeFoods= new ArrayList<>();

        if(maxDist != null && lon != null && lat != null){
            Double disDiff;
            for(Food food: foods){
                LocationFood lf = locationFoodDao.findByFood_id(food.getId());
                disDiff = distFrom(lon, lat, lf.getLon(), lf.getLat())/1000;
                if(disDiff > maxDist){
                    filterByDistanceFoods.add(food);
                }
            }
            if(filterByDistanceFoods.size() > 0) {
                foods.removeAll(filterByDistanceFoods);
            }
        }

        if(maxPrice != null){
            for(Food f: foods){
                if(f.getPrice() > maxPrice){
                    filterByMaxPriceFoods.add(f);
                }
            }
            if(filterByMaxPriceFoods.size() > 0){
                foods.removeAll(filterByMaxPriceFoods);
            }
        }

        if(foodType != null){
            for(Food f: foods){
                if(!f.getFood_type().equalsIgnoreCase(foodType) ){
                    filterByFoodTypeFoods.add(f);
                }
            }
            if(filterByFoodTypeFoods.size() > 0) {
                foods.removeAll(filterByFoodTypeFoods);
            }
        }

        if(cuisineType != null){
            for(Food f: foods){
                if(!f.getCuisine_type().equalsIgnoreCase(cuisineType) ){
                    filterByCuisineTypeFoods.add(f);
                }
            }
            if(filterByCuisineTypeFoods.size() > 0) {
                foods.removeAll(filterByCuisineTypeFoods);
            }
        }

        log.info("Filtered result size: " + foods.size());

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

    public File inputStream2file (InputStream in, String filename, String suffix) throws IOException {
        final File tempFile = File.createTempFile(filename, suffix);
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return tempFile;
    }
}