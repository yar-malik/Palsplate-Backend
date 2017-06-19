package com.jersey.persistence;

import com.jersey.representations.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PersonDao extends JpaRepository<Person, Long> {



}
