package com.jersey.Authorization.security;

import com.jersey.persistence.LoginDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

@Component
public class UserPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private LoginDao loginDaoRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {

        System.out.println("In the first has permission");
        System.out.println("Authorities: " + authentication.getAuthorities());
        System.out.println("TargetObject: " + targetDomainObject);
        System.out.println("Permission: " + permission);

        return true;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String resourceType, Object allowedPermissions) {

        if(resourceType.equals("CustomerResource"))
        {
            return checkCustomerResourcePermission(authentication, (long) serializable, (String) allowedPermissions);
        }

        return false;
    }

    public boolean checkCustomerResourcePermission(Authentication authentication, long ID, String allowedPermissions)
    {
        ArrayList<String> permissionsArray = getPermissions(allowedPermissions);
        return true;
    }

    public ArrayList<String> getPermissions(String permissions)
    {
        ArrayList<String> permissionsArray = new ArrayList<String>();
        StringTokenizer tokens = new StringTokenizer(permissions, ",");

        while(tokens.hasMoreTokens())
        {
            permissionsArray.add(tokens.nextToken());
        }

        return permissionsArray;
    }
}
