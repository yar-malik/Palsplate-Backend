package com.jersey.persistence;

import com.jersey.representations.LocationPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface LocationPersonDao extends JpaRepository<LocationPerson, Long> {

    @Query("SELECT u FROM LocationPerson u WHERE (u.person_id) = (:person_id)")
    LocationPerson findByPerson_id(@Param("person_id") Long person_id);

}
