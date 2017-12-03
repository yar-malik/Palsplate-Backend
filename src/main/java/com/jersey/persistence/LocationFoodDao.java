package com.jersey.persistence;

import com.jersey.representations.LocationFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationFoodDao extends JpaRepository<LocationFood, Long> {

    @Query("SELECT u FROM LocationFood u WHERE (u.food_id) = (:food_id)")
    LocationFood findByFood_id(@Param("food_id") Long food_id);

}
