package com.jersey.persistence;

import com.jersey.representations.LocationPerson;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LocationPersonDao extends JpaRepository<LocationPerson, Long> {
}
