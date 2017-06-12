package com.jersey.persistence;

import com.jersey.representations.Login;
import com.jersey.representations.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LoginDao extends JpaRepository<Login, Long> {

    Login findByUsername(String username);
}