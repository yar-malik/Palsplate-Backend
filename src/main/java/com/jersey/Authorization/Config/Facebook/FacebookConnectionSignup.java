package com.jersey.Authorization.Config.Facebook;

import com.jersey.persistence.PersonDao;
import com.jersey.representations.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class FacebookConnectionSignup implements ConnectionSignUp{

    @Autowired
    private PersonDao personRepository;

    @Override
    public String execute(Connection<?> connection) {

        System.out.println("Name: " + connection.getDisplayName());
        System.out.println("ImageURL: " + connection.getImageUrl());
        System.out.println("ProfileURL: " + connection.getProfileUrl());
        System.out.println("Username: " + connection.fetchUserProfile().getUsername());
        System.out.println("Email: " + connection.fetchUserProfile().getEmail());
        System.out.println("ID: " + connection.fetchUserProfile().getId());

        /*
        Person person = new Person();
        person.setFirstName(connection.getDisplayName());
        person.setPassword(randomAlphabetic(8));
        personRepository.save(person);*/
//        return person.getFirstName();
        return "hi";
    }
}
