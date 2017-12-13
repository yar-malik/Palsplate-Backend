package com.jersey.Authorization.Config.Facebook;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.jersey.Authorization.security.Authorities;
import com.jersey.persistence.PersonDao;
import com.jersey.representations.Person;
import com.jersey.resources.PersonResource;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Map;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@Service
public class FacebookConnectionSignup implements ConnectionSignUp{

    private static final Logger log = LogManager.getLogger(PersonResource.class);

    @Autowired
    private PersonDao personRepository;

    @Autowired
    private Cloudinary cloudinary;

    private final int IMAGE_WIDTH = 640;

    private final int IMAGE_HEIGHT = 640;


    @Override
    public String execute(Connection<?> connection) {

        //Get the user properties from facebook apis
        Facebook facebook = (Facebook) connection.getApi();
        String [] fields = { "id", "email",  "first_name", "last_name" , "location", "about", "gender"};
        User userProfile =  facebook.fetchObject("me",User.class, fields);

        Person person = personRepository.findByEmail(userProfile.getEmail());



        if(person == null){
            //create a new user in our local db if it already doesn't exist
            person = new Person();
            person.setFirstName(userProfile.getFirstName());
            person.setLastName(userProfile.getLastName());
            person.setEmail(userProfile.getEmail());
            person.setRoles(Authorities.ROLE_USER_FACEBOOK.name());
            person.setPassword(randomAlphabetic(8));
            person.setGender(userProfile.getGender());
            UploadFacebookProfilePicToCloudinary(connection, person);

            if(userProfile.getAbout() != null){
                person.setDescription(userProfile.getAbout());
            }
            else{
                person.setDescription("");
            }

            person.setPhoneNumber("");

            personRepository.save(person);
        }

        return person.getEmail();
    }

    private void UploadFacebookProfilePicToCloudinary(Connection<?> connection, Person person){

        String profileImageURLLarge = new StringBuilder(connection.getImageUrl())
                .append("?").append("width=").append(IMAGE_WIDTH)
                .append("&").append("height=").append(IMAGE_HEIGHT)
                .toString();

        File tempFile;
        Map uploadResult = null;

        try {
            tempFile = File.createTempFile("profilepic", ".png");
            tempFile.deleteOnExit();
            FileUtils.copyURLToFile(new URL(profileImageURLLarge), tempFile);
            uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.emptyMap());

        } catch (IOException e) {
            e.printStackTrace();
        }


        log.info("cloudinary secure_url: " + uploadResult.get("secure_url"));
        log.info("cloudinary public_id: " + uploadResult.get("public_id"));
        log.info("cloudinary original_filename: " + uploadResult.get("original_filename"));

        person.setPhotoName(new StringBuilder((String)uploadResult.get("original_filename")).append(new Date().toString()).append(".png").toString());
        person.setPhotoPublicId(uploadResult.get("public_id").toString());
    }
}