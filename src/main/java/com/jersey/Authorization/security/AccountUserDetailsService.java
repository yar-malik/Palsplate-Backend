package com.jersey.Authorization.security;

import com.jersey.persistence.LoginDao;
import com.jersey.representations.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

//import javax.inject.Inject;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by muhammad on 6/11/17.
 */
@Component("userDetailsService")
public class AccountUserDetailsService implements UserDetailsService {


    @Autowired
    private LoginDao accountRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Login person = accountRepository.findByUsername(username);

        if (person == null) {
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }

        Collection<GrantedAuthority> grantedAuthorities = new ArrayList();
        GrantedAuthority g = new SimpleGrantedAuthority("ROLE_USER");
        grantedAuthorities.add(g);
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        return new User(person.getUserName(), person.getPassword(), grantedAuthorities);

    }
}
