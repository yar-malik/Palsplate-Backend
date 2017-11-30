package com.jersey.Authorization.Config.Facebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.web.context.request.NativeWebRequest;

public class FacebookSignInAdapter implements SignInAdapter{

    @Autowired
    private AuthorizationServerTokenServices authTokenServices;

/*    private OAuth2Authentication UserDetailsToAuthentication(NativeWebRequest request){
        return new OAuth2Authentication(request, new UsernamePasswordAuthenticationToken());
    }
*/
    @Override
    public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {

        //Get the user properties from facebook apis
        Facebook facebook = (Facebook) connection.getApi();
        String [] fields = { "id", "email",  "first_name", "last_name" , "location", "about"};
        User userProfile =  facebook.fetchObject("me",User.class, fields);

        System.out.println("localuserid: " + localUserId);


      //  OAuth2AccessToken oAuth2AccessToken = authTokenServices.createAccessToken();

//        HttpServletRequest response = ((ServletWebRequest)request).getRequest();
//        System.out.println("Principal name: " + response.getUserPrincipal().getName());

        return "www.bing.com";
    }


}
