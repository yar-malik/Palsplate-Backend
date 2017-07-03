package com.jersey.Authorization.security;

/**
 * Created by muhammad on 6/13/17.
 */
import com.jersey.Authorization.security.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
