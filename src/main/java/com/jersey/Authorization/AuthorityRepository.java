package com.jersey.Authorization;

/**
 * Created by muhammad on 6/13/17.
 */
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
