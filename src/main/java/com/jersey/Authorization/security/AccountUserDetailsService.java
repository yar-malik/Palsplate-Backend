package com.jersey.Authorization.security;

import com.jersey.persistence.LoginDao;
import com.jersey.representations.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

@Component
public class AccountUserDetailsService implements UserDetailsService {

    @Autowired
    private LoginDao loginDaoRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Login person = loginDaoRepository.findByEmail(email);

        if (person == null)
        {
            throw new UsernameNotFoundException("User with email address " + email + " was not found. Did you forget your registered email?");
        }

        Collection<GrantedAuthority> grantedAuthorities = new ArrayList();
        String personRoles = person.getRoles();
        StringTokenizer tokens = new StringTokenizer(personRoles);

        while(tokens.hasMoreTokens())
        {
            grantedAuthorities.add(new SimpleGrantedAuthority(tokens.nextToken()));
        }

        return new User(person.getEmail(), person.getPassword(), grantedAuthorities);
    }
}
