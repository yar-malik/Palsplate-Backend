package com.jersey.Authorization.Config;

import com.jersey.Authorization.security.UserPermissionEvaluator;
import com.jersey.resources.LoggingFilter;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@ComponentScan("com.jersey.Authorization")
public class CustomMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration{

    @Autowired
    private UserPermissionEvaluator userPermissionEvaluator;

    @Bean
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {

        LogManager.getLogger(LoggingFilter.class).info("Initializing the CustomMethodSecurityConfiguration ...");

        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(userPermissionEvaluator);
        return expressionHandler;
    }

}
