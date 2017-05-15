package com.jersey.persistence;

import com.jersey.representations.Person;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PersonDao extends JpaRepository<Person, Long> {
}
