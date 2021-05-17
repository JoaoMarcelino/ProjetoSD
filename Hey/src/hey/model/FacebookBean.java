package hey.model;
import java.text.ParseException;

import java.util.Random;
import java.util.Scanner;
import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.io.IOException;

public class FacebookBean {

    private FacebookREST fb;
    private String authUrl;
    private String authCode;
    private String secretState;
    private OAuth2AccessToken accessToken;
    private String name;
    private String test = "Ola Mundo!";


    public String getTest(){
        return this.test;
    }


    public FacebookBean() {
        System.out.println("bean");
        this.fb = new FacebookREST();
    }

    public boolean getAccessToken() {
        this.accessToken = this.fb.getAccessToken(this.authCode, this.secretState);
        if (this.accessToken != null)
            return true;
        return false;
}

    public String getAuthUrl() {
        System.out.println("authURL");
        this.authUrl = this.fb.getAuthorizationURL();
        return authUrl;
    }


    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getSecretState() {
        return secretState;
    }

    public void setSecretState(String secretState) {
        this.secretState = secretState;
    }


    public String getName() throws ParseException {
        return this.fb.getAccountName(this.accessToken);
    }

    public void setName(String name) {
        this.name = name;
    }



}
