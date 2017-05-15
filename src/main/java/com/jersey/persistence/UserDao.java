package com.jersey.persistence;

import com.jersey.representations.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserDao extends JpaRepository<User, Long> {
}
