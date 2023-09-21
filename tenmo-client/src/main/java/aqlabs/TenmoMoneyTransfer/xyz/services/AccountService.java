package aqlabs.TenmoMoneyTransfer.xyz.services;

import aqlabs.TenmoMoneyTransfer.xyz.model.Account;
import aqlabs.TenmoMoneyTransfer.xyz.model.AuthenticatedUser;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


public class AccountService {

    public static final String API_URL = "http://localhost:8080/user/account";
    private final RestTemplate RESTTEMPLATE = new RestTemplate();

    private final AuthenticatedUser currentUser;

    public AccountService(AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
    }


    //gets a single account by feeding in a USERID
    public Account getAccount(int id){
        ResponseEntity<Account> response = RESTTEMPLATE.exchange(API_URL+"/id/" + id ,HttpMethod.GET ,createEntity(currentUser), Account.class);
        return response.getBody();
    }



    //-

    private HttpEntity<Void> createEntity(AuthenticatedUser currentUser){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<Void>(headers);
    }



}
