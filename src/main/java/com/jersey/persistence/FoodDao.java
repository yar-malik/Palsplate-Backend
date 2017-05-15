package com.jersey.persistence;

import com.jersey.representations.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodDao extends JpaRepository<Food, Long> {
}
