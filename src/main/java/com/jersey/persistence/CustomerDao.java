package com.jersey.persistence;

import com.jersey.representations.Customer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerDao extends JpaRepository<Customer, Long> {
}
