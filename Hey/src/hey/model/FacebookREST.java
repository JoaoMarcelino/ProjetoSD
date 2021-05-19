package hey.model;

import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class FacebookREST {

    public static final String NETWORK_NAME = "Facebook";
    public static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/v3.2/me";
    public String pathToProperties="../webapps/Hey/WEB-INF/classes/resources/config.properties";
    public static String apiKey;
    public static String apiSecret;
    public OAuth2AccessToken accessToken;
    public String secretState;
    public OAuth20Service serviceLogin;
    public OAuth20Service serviceAssociate;

    public FacebookREST(){
        loadProperties();
         this.serviceLogin = new ServiceBuilder(apiKey)
                .apiSecret(apiSecret)
                .callback("http://localhost:8080/Hey/fblogin.action")
                .build(FacebookApi.instance());
        this.serviceAssociate = new ServiceBuilder(apiKey)
                .apiSecret(apiSecret)
                .callback("http://localhost:8080/Hey/fbassociate.action")
                .build(FacebookApi.instance());
         this.secretState = "secret" + new Random().nextInt(999_999);
    }

    public String getAssociationURL() {
        return this.serviceAssociate.getAuthorizationUrl(secretState);
    }

    public String getLoginURL() {
        return this.serviceLogin.getAuthorizationUrl(secretState);
    }

    public void setAccessToken(String code){
        try{
            accessToken = serviceLogin.getAccessToken(code);
        } catch (ExecutionException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setAccessTokenAssociation(String code){
        try{
            accessToken = serviceAssociate.getAccessToken(code);
        } catch (ExecutionException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public OAuth2AccessToken getAccessToken(){
        return this.accessToken;
    }

    public void setSecretState(String state){
        this.secretState=state;
    }

    public String getAccountName(){
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        this.serviceLogin.signRequest(accessToken, request);

        try(Response response = this.serviceLogin.execute(request)){
            String reply = response.getBody();
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(reply);
            System.out.println("HTTP RESPONSE: =============");
            System.out.println(response.getCode());
            System.out.println(response.getBody());
            System.out.println("END RESPONSE ===============");
            return(String) json.get("name");
        } catch (IOException | ExecutionException | InterruptedException | ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getAccountId(){
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        this.serviceLogin.signRequest(accessToken, request);

        try(Response response = this.serviceLogin.execute(request)){
            String reply = response.getBody();
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(reply);
            System.out.println("HTTP RESPONSE: =============");
            System.out.println(response.getCode());
            System.out.println(response.getBody());
            System.out.println("END RESPONSE ===============");
            return(String) json.get("id");
        } catch (IOException | InterruptedException | ParseException | ExecutionException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void loadProperties() {
        try {
            InputStream input = new FileInputStream(pathToProperties);
            Properties prop = new Properties();
            prop.load(input);
            this.apiKey = prop.getProperty("apiKey");
            this.apiSecret = prop.getProperty("apiSecret");
        }catch (FileNotFoundException e){
            System.out.println("Ficheiro de configurações não encontrado");
        }catch (IOException e){
            System.out.println("Erro na leitura de ficheiro de configurações");
        }
    }
}
