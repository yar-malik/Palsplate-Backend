package com.jersey.Authorization.security;

import com.jersey.persistence.PersonDao;
import com.jersey.representations.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AccountUserDetailsService implements UserDetailsService {

    @Autowired
    private PersonDao personDaoRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Person person = personDaoRepository.findByEmail(email);

        if (person == null){
            throw new UsernameNotFoundException("User with emailaddress " + email + " was not found. Did you forget your registered email?");
        }

        return new User(person.getEmail(), person.getPassword(), person.getGrantedAuthorities());
    }
}
