package aqlabs.TenmoMoneyTransfer.xyz.services;

import aqlabs.TenmoMoneyTransfer.xyz.model.User;
import aqlabs.TenmoMoneyTransfer.xyz.model.AuthenticatedUser;
import utils.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class UserService {
    public final String API_URL = "http://localhost:8080/user";

    public final RestTemplate RESTTEMPLATE = new RestTemplate();

    private AuthenticatedUser authenticatedUser;

    public UserService (AuthenticatedUser authenticatedUser) {this.authenticatedUser = authenticatedUser;}


    //method to GET all users from the server's endpoint http://localhost:8080/users
    public User[] getAllUsers(){
        User[] users = {};
        try {
            ResponseEntity<User[]> response = RESTTEMPLATE.exchange(API_URL, HttpMethod.GET, makeAuthEntity(), User[].class);
            users = response.getBody();
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return users;
    }

    //method to GET a user by feeding in a UserID to the server's endpoint http://localhost:8080/users/user/{id}
    public User getUser(int id){
        User user = null;
        try {
            ResponseEntity<User> response = RESTTEMPLATE.exchange(API_URL+"/user/"+id, HttpMethod.GET, makeAuthEntity(), User.class);
            user = response.getBody();
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return user;
    }

    //method creates the httpEntity with authentication token
    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        return new HttpEntity<>(headers);
    }


}
