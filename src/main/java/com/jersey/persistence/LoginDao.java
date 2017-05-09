package com.jersey.persistence;

import com.jersey.representations.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginDao extends JpaRepository<Login, Long> {
}