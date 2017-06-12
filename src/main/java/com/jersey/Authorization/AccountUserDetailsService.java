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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by muhammad on 6/11/17.
 */
@Service("accountdetailsservice")
public class AccountUserDetailsService implements UserDetailsService {


    @Autowired
    LoginDao accountRepository;

    public AccountUserDetailsService(){
        accountRepository = null;
    }
    public AccountUserDetailsService(LoginDao accountRepository){
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       // Login person = accountRepository.findByUsername(username);
        Login person = new Login(new Long(1234),"username", "password");
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        GrantedAuthority g = new SimpleGrantedAuthority("password");
        grantedAuthorities.add(g);

        return new User(person.getUserName(), person.getPassword(), grantedAuthorities);

    }
}
