package com.jersey.Authorization.Config;

import com.jersey.Authorization.security.UserPermissionEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class CustomMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration{

    @Autowired
    private UserPermissionEvaluator userPermissionEvaluator;

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {

        System.out.println("\n\n\nThis being executed\n\n\n");

        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(userPermissionEvaluator);
        return expressionHandler;
    }

}
