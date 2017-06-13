package com.jersey.Authorization;

import com.jersey.persistence.LoginDao;
import com.jersey.persistence.PersonDao;
import com.jersey.representations.Login;
import com.jersey.representations.Person;
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

import javax.inject.Inject;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by muhammad on 6/11/17.
 */
@Component
public class AccountUserDetailsService implements UserDetailsService {


    @Autowired
    private LoginDao accountRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("Username: " + username);

       // Login person = accountRepository.findByUsername(username);
        Login person = new Login(new Long(1234),"username", "password");

        String password = String.format("%040x", new BigInteger(1, new String("password").getBytes()));

        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        GrantedAuthority g = new SimpleGrantedAuthority("ROLE_USER");
        grantedAuthorities.add(g);

        return new User("html5", password, grantedAuthorities);

    }
}
