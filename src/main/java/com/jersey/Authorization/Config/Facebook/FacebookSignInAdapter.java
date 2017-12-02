package com.jersey.Authorization.Config.Facebook;

import com.jersey.persistence.PersonDao;
import com.jersey.representations.Person;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

public class FacebookSignInAdapter implements SignInAdapter{


    private final AuthorizationServerTokenServices authTokenServices;
    private final PersonDao personDao;
    private final String localClientID;

    public FacebookSignInAdapter(AuthorizationServerTokenServices authorizationServerTokenServices, PersonDao personDao, String localClientID){

        this.authTokenServices = authorizationServerTokenServices;
        this.personDao = personDao;
        this.localClientID = localClientID;
    }

    private OAuth2Authentication PersonToAuthentication(Person person){

        OAuth2Request request = new OAuth2Request(null, localClientID, person.getGrantedAuthorities(), true, null, null, null, null, null);
        return new OAuth2Authentication(request, new UsernamePasswordAuthenticationToken(person.getEmail(), "N/A", person.getGrantedAuthorities()));
    }

    @Override
    public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {

        Person person = personDao.findByEmail(localUserId);
        OAuth2AccessToken oAuth2AccessToken = authTokenServices.createAccessToken(PersonToAuthentication(person));

        String redirectUrl = new StringBuilder("https://www.palsplate.com/authenticate")
                                .append("?").append("access_token").append("=")
                                .append(oAuth2AccessToken.getValue())
                                .append("&").append("user_id").append("=")
                                .append(person.getId())
                                .append("&").append("expires_at").append("=")
                                .append(oAuth2AccessToken.getExpiresIn())
                                .toString();
        return redirectUrl;
    }


}
