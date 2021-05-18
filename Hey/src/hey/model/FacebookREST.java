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

    private static final String NETWORK_NAME = "Facebook";
    private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/v3.2/me";
    private String pathToProperties="../webapps/Hey/WEB-INF/classes/resources/config.properties";
    private static String apiKey;
    private static String apiSecret;
    private OAuth20Service service;
    private String secretState;

    public FacebookREST(){
        loadProperties();
         this.service = new ServiceBuilder(apiKey)
                .apiSecret(apiSecret)
                .callback("http://localhost:8080/Hey/fblogin.action")
                .build(FacebookApi.instance());
         this.secretState = "secret" + new Random().nextInt(999_999);
    }

    public String getAuthorizationURL() {
        String authorizationURL = this.service.getAuthorizationUrl(secretState);
        return authorizationURL;
    }

    public OAuth2AccessToken getAccessToken(String authCode, String secretState){
        OAuth2AccessToken accessToken = null;

        try{
            accessToken = service.getAccessToken(authCode);
        } catch (ExecutionException | IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    public String getAccountName(OAuth2AccessToken accessToken){
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        this.service.signRequest(accessToken, request);

        try(Response response = this.service.execute(request)){
            String reply = response.getBody();
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(reply);
            System.out.println("HTTP RESPONSE: =============");
            System.out.println(response.getCode());
            System.out.println(response.getBody());
            System.out.println("END RESPONSE ===============");
            return(String) json.get("name");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException | ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getAccountId(OAuth2AccessToken accessToken){
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        this.service.signRequest(accessToken, request);

        try(Response response = this.service.execute(request)){
            String reply = response.getBody();
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(reply);
            System.out.println("HTTP RESPONSE: =============");
            System.out.println(response.getCode());
            System.out.println(response.getBody());
            System.out.println("END RESPONSE ===============");
            return(String) json.get("id");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException | ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String voteAppeal(OAuth2AccessToken accessToken){
        OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL + "/feed");
        System.out.println("voto");
        request.addHeader("Content-Type", "application/json");
        request.setPayload("{\n" +
                "    \"message\": " + "Votem Cornos" +",\n" +
                "    \"link\": http://localhost:8080/Hey/listResultados.action,\n" +
                "}");
        this.service.signRequest(accessToken, request);

        try(Response response = this.service.execute(request)){
            String reply = response.getBody();
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(reply);
            System.out.println("HTTP RESPONSE: =============");
            System.out.println(response.getCode());
            System.out.println(response.getBody());
            System.out.println("END RESPONSE ===============");
            return(String) json.get("id");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException | ParseException e) {
            e.printStackTrace();
        }
        return "";

    }

    public String shareResults(OAuth2AccessToken accessToken){
        OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL + "/feed");
        System.out.println("resultados");

        request.addHeader("Content-Type", "application/json");
        request.setPayload("{\n" +
                "    \"message\": " + "RESULTADOS: " +",\n" +
                "    \"access_token\": " + accessToken +",\n" +
                "}");
        service.signRequest(accessToken, request);
        try(Response response = this.service.execute(request)){
            String reply = response.getBody();
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(reply);

            return(String) json.get("id");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException | ParseException e) {
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
