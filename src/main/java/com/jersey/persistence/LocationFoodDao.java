package com.jersey.persistence;

import com.jersey.representations.LocationFood;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LocationFoodDao extends JpaRepository<LocationFood, Long> {
}
