package com.jersey.Authorization.security;


import com.jersey.representations.Person;
import org.hibernate.validator.constraints.Email;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PasswordResetTokenStore {

    private static PasswordResetTokenStore instance = null;

    private ArrayList<PasswordResetToken> tokens;

    private PasswordResetTokenStore(){
        tokens = new ArrayList<PasswordResetToken>();
    }

    public static PasswordResetTokenStore getInstance(){

        if(instance == null){
            instance = new PasswordResetTokenStore();
        }

        return instance;
    }

    public void addToken(String Principal, String token){
        tokens.add(new PasswordResetToken(Principal, token));
    }

    public boolean removeToken(String resetToken){
        PasswordResetToken token = getPasswordResetToken(resetToken);

        if(token == null){
            return true;
        }

        return tokens.remove(token);
    }

    public PasswordResetToken getTokenFromEmail(@Email String Email){

        for(PasswordResetToken token : tokens){
            if(token.getPrincipal().equalsIgnoreCase(Email)){
                return token;
            }
        }

        return null;
    }

    public void updateResetToken(@Email String Email, String resetToken){

        for(PasswordResetToken token : tokens){
           if(token.getPrincipal().equalsIgnoreCase(Email)){
               token.setResetToken(resetToken);
           }
        }
    }

    public boolean isValid(Person person, String resetToken){

        PasswordResetToken token = getPasswordResetToken(resetToken);

        if(token == null){
            return false;
        }

        if(!token.getPrincipal().equalsIgnoreCase(person.getEmail())){
            return false;
        }

        Calendar cal = Calendar.getInstance();

        if ((token.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return false;
        }

        return true;
    }

    private PasswordResetToken getPasswordResetToken(String resetToken){

        for(PasswordResetToken token : tokens){
            if(token.getResetToken().equals(resetToken)){
                return token;
            }
        }

        return null;
    }

}
