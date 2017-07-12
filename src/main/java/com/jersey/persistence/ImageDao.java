package com.jersey.persistence;

import com.jersey.representations.Image;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ImageDao extends JpaRepository<Image, Long> {
}
