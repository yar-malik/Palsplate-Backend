package com.jersey.persistence;

import com.jersey.representations.Cook;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CookDao extends JpaRepository<Cook, Long> {
}
