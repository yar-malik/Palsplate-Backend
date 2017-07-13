package com.jersey.persistence;

import com.jersey.representations.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewDao extends JpaRepository<Review, Long> {
}
