package com.jersey.Authorization.Config.Facebook;

import com.jersey.persistence.PersonDao;
import com.jersey.representations.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@Service
public class FacebookConnectionSignup implements ConnectionSignUp{

    @Autowired
    private PersonDao personRepository;

    @Override
    public String execute(Connection<?> connection) {

        //Get the user properties from facebook apis
        Facebook facebook = (Facebook) connection.getApi();
        String [] fields = { "id", "email",  "first_name", "last_name" , "location", "about"};
        User userProfile =  facebook.fetchObject("me",User.class, fields);

        Person person = personRepository.findByEmail(userProfile.getEmail());

        if(person == null){
            //create a new user in our local db if it already doesn't exist
            person = new Person();
            person.setFirstName(userProfile.getFirstName());
            person.setLastName(userProfile.getLastName());
            person.setEmail(userProfile.getEmail());
            person.setRoles("ROLE_USER_FACEBOOK");
            person.setPassword(randomAlphabetic(8));

            if(userProfile.getLocation() != null){
                person.setAddress(userProfile.getLocation().getName());
            }
            else{
                //temp since we have not null constraint
                person.setAddress("This is a placeholder for address");
            }

            if(userProfile.getAbout() != null){
                person.setDescription(userProfile.getAbout());
            }
            else{
                //temp because we have not null constraint
                person.setDescription("This is a placeholder for description");
            }

            //temp placeholder
            person.setPhoneNumber("123456789");

            personRepository.save(person);
        }

        return person.getEmail();
    }
}
