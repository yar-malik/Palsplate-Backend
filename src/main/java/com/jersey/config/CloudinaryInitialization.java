package com.jersey.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryInitialization {

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "palsplate",
                "api_key", System.getenv().get("CLOUDINARY_APIKEY"),
                "api_secret", System.getenv().get("CLOUDINARY_APISECRET"),
                "secure", true));
    }
}
