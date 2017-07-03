package com.jersey.persistence;

import com.jersey.representations.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoginDao extends JpaRepository<Login, Long> {

    @Query("SELECT u FROM Login u WHERE LOWER(u.username) = LOWER(:username)")
    Login findByUsername(@Param("username") String username);
}