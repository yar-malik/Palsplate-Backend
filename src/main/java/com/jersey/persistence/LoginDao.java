package com.jersey.persistence;

import com.jersey.representations.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoginDao extends JpaRepository<Login, Long> {

    @Query("SELECT u FROM Login u WHERE LOWER(u.email) = LOWER(:email)")
    Login findByEmail(@Param("email") String email);
}