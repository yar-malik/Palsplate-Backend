package com.jersey.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;


public class CloudinaryInitialization {

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "palsplate",
                "api_key", "816138784777145",
                "api_secret", "tA4kTPJ029PlpmCb7drT-_7RHUM",
                "secure", true));
    }
}
