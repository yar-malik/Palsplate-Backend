package com.jersey;

import com.jersey.config.AppErrorController;
import com.jersey.config.JerseyInitialization;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.jhades.JHades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {

    @Autowired
    private ErrorAttributes errorAttributes;

    @Bean
    public AppErrorController appErrorController(){return new AppErrorController(errorAttributes);}

    public static void main(String[] args) {

        new JHades().overlappingJarsReport();
        new SpringApplicationBuilder(Application.class).run(args);
    }

    @Bean
    public ServletRegistrationBean jerseyServlet() {
        Map<String,String> params = new HashMap<String,String>();
        params.put("javax.ws.rs.Application","com.verico.multipart.app.MultiPartApp");
        params.put("jersey.config.server.provider.classnames","org.glassfish.jersey.filter.LoggingFilter;org.glassfish.jersey.media.multipart.MultiPartFeature");
        ServletRegistrationBean registration = new ServletRegistrationBean(new ServletContainer(), "/resources");
        registration.setInitParameters(params);
        registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, JerseyInitialization.class.getName());
        return registration;
    }
}
