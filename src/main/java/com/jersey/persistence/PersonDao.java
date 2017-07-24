package com.jersey.persistence;

import com.jersey.representations.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PersonDao extends JpaRepository<Person, Long> {

    @Query("SELECT u FROM Person u WHERE LOWER(u.email) = LOWER(:email)")
    Person findByEmail(@Param("email") String email);

}
