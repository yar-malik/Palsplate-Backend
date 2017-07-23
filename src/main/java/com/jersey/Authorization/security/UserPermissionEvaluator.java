package com.jersey.Authorization.security;

import com.jersey.persistence.*;
import com.jersey.representations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

@Component
public class UserPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private LoginDao loginDaoRepository;

    @Autowired
    private CustomerDao customerDaoRepository;

    @Autowired
    private PersonDao personDaoRepository;

    @Autowired
    private CookDao cookDaoRepository;

    @Autowired
    private FoodDao foodDaoRepository;

    @Autowired
    private ReviewDao reviewDaoRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object allowedPermissions) {

        if(checkIfUserIsAdmin(authentication))
        {
            return true;
        }

        //otherwise check if the current user has the required role to make this call
        ArrayList<String> allowedPermissionsArray = getPermissions((String)allowedPermissions);
        ArrayList<GrantedAuthority> userPermissions = new ArrayList<GrantedAuthority>(authentication.getAuthorities());

        boolean userHasGrantedRole = false;

        for(String permission : allowedPermissionsArray)
        {
            for(GrantedAuthority userPermission : userPermissions)
            {
                if(permission.equals(userPermission.getAuthority()))
                {
                    userHasGrantedRole = true;
                }
            }
        }

        return userHasGrantedRole;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String resourceType, Object allowedPermissions) {

        if(checkIfUserIsAdmin(authentication))
        {
            return true;
        }

        //otherwise check if the current user has the required role to make this call
        ArrayList<String> allowedPermissionsArray = getPermissions((String)allowedPermissions);
        ArrayList<GrantedAuthority> userPermissions = new ArrayList<GrantedAuthority>(authentication.getAuthorities());

        boolean userHasGrantedRole = false;

        for(String permission : allowedPermissionsArray)
        {
            for(GrantedAuthority userPermission : userPermissions)
            {
                if(permission.equals(userPermission.getAuthority()))
                {
                    userHasGrantedRole = true;
                }
            }
        }

        if(!userHasGrantedRole)
        {
            return false;
        }

        // check whether the user is making a call to only their own resource or not
        if(resourceType.equals("CustomerResource"))
        {
            return checkCustomerResourcePermission(authentication, (long) serializable);
        }

        if(resourceType.equals("CookResource"))
        {
            return checkCookResoucePermission(authentication, (long) serializable);
        }

        if(resourceType.equals("FoodResource"))
        {
            return checkFoodResourcePermission(authentication, (long) serializable);
        }

        if(resourceType.equals("LoginResource"))
        {
            return checkLoginResourcePermission(authentication, (long) serializable);
        }

        if(resourceType.equals("PersonResource"))
        {
            return checkPersonResourcePermission(authentication, (long) serializable);
        }

        if(resourceType.equals("ReviewResource"))
        {
            return checkReviewResourcePermission(authentication, (long) serializable);
        }

        return false;
    }

    public boolean checkCustomerResourcePermission(Authentication authentication, long ID)
    {
        // find the login id of the requested resource
        Customer customer = customerDaoRepository.findOne(ID);
        Person person = personDaoRepository.findOne(customer.getPerson_id());

        // find the login id of the user who requested the resource
        Login currentLogin = loginDaoRepository.findByEmail(authentication.getName());

        //check if they are equal
        return currentLogin.getId() == person.getLogin_id();
    }

    public boolean checkCookResoucePermission(Authentication authentication, long ID)
    {
        // find the login id of the requested resource
        Cook cook = cookDaoRepository.getOne(ID);
        Person person = personDaoRepository.getOne(cook.getPerson_id());

        // find the login id of the user who requested the resource
        Login currentLogin = loginDaoRepository.findByEmail(authentication.getName());

        //check if they are equal
        return currentLogin.getId() == person.getLogin_id();

    }

    public boolean checkFoodResourcePermission(Authentication authentication, long ID)
    {
        Food food = foodDaoRepository.getOne(ID);
        return checkCookResoucePermission(authentication, food.getCook_id());
    }

    public boolean checkLoginResourcePermission(Authentication authentication, long ID)
    {
        Login login = loginDaoRepository.getOne(ID);

        // find the login id of the user who requested the resource
        Login currentLogin = loginDaoRepository.findByEmail(authentication.getName());

        //check if they are equal
        return currentLogin.getId() == login.getId();

    }

    public boolean checkPersonResourcePermission(Authentication authentication, long ID)
    {
        Person person = personDaoRepository.getOne(ID);

        // find the login id of the user who requested the resource
        Login currentLogin = loginDaoRepository.findByEmail(authentication.getName());

        //check if they are equal
        return currentLogin.getId() == person.getLogin_id();
    }

    public boolean checkReviewResourcePermission(Authentication authentication, long ID)
    {
        Review review = reviewDaoRepository.findOne(ID);

        // find the login id of the user who requested the resource
        Login currentLogin = loginDaoRepository.findByEmail(authentication.getName());

        // TODO add a functionality to link a review with a person
        //check if they are equal
     //   return currentLogin.getId() == person.getLogin_id();
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

    public boolean checkIfUserIsAdmin(Authentication authentication)
    {
        ArrayList<GrantedAuthority> userPermissions = new ArrayList<GrantedAuthority>(authentication.getAuthorities());

        for(GrantedAuthority authority : userPermissions)
        {
            if(authority.getAuthority().equals("ROLE_ADMIN"))
            {
                // if the resource requester is an admin then allow all requests
                return true;
            }
        }

        return false;
    }
}
