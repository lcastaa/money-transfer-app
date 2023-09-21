package aqlabs.TenmoMoneyTransfer.xyz.services;

import aqlabs.TenmoMoneyTransfer.xyz.model.Transfer;
import aqlabs.TenmoMoneyTransfer.xyz.model.AuthenticatedUser;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class TransferService {
    private static final String API_BASE_URL = "http://localhost:8080/transfer/";
    private static final RestTemplate restTemplate = new RestTemplate();

    private AuthenticatedUser currentUser;

    public TransferService(AuthenticatedUser currentUser){
        this.currentUser = currentUser;
    }


    public Transfer getATransfer(int transferId){
        Transfer transfer = new Transfer();
        ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL+transferId,HttpMethod.GET,makeAuthEntity(currentUser),Transfer.class);
        return transfer = response.getBody();
    }

    public Transfer[] getTransfers(int userId, int filter){
        Transfer[] transfers = {};
        ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL+"/list/id/+"+userId+"/"+filter,HttpMethod.GET,makeAuthEntity(currentUser),Transfer[].class);
        return transfers = response.getBody();
    }

    public void sendInstant(Transfer transfer){
        restTemplate.exchange(API_BASE_URL+"/send",HttpMethod.POST,makeTransferEntity(currentUser, transfer),Transfer.class);
    }

    public void sendRequest(Transfer transfer){
        restTemplate.exchange(API_BASE_URL+"/send",HttpMethod.POST,makeTransferEntity(currentUser, transfer),Transfer.class);
    }

    public void sendUpdatedRequest(Transfer transfer){
        restTemplate.exchange(API_BASE_URL+"/update",HttpMethod.PUT,makeTransferEntity(currentUser, transfer),Transfer.class);
    }








    //makes the httpEntity with authenticated token
    private HttpEntity<?> makeAuthEntity(AuthenticatedUser currentUser) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());;
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Transfer> makeTransferEntity(AuthenticatedUser currentUser, Transfer transfer){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<Transfer>(transfer, headers);
    }







//    //method to GET all transfers from the server's endpoint http://localhost:8080/transfer/sent/{accountID}
//    public Transfer[] getTransferById(Account account){
//        Transfer[] transfers = null;
//        try {
//           ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL+"/sent/"+account.getAccountId(), HttpMethod.GET, makeAuthEntity(), Transfer[].class);
//           transfers = response.getBody();
//        } catch (RestClientResponseException e) {
//            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
//        } catch (ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
//        }
//        return transfers;
//    }
//
//
//    //method to POST a transfer to the server's endpoint http://localhost:8080/transfer/send
//    public void createSendTransfer(Transfer transfer) {
//        HttpEntity<Transfer> entity = makeTransferEntity(transfer);
//        Transfer sendingTransfer = null;
//        try {
//            sendingTransfer = restTemplate.postForObject(API_BASE_URL + "/send", entity, Transfer.class);
//        } catch (RestClientResponseException e) {
//            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
//        } catch (ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
//        }
//    }
//
//
//    //method to GET a transfer object from the server's endpoint http://localhost:8080/transfer/details/{id}
//    public Transfer viewTransfer(int id){
//        Transfer transfer = null;
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(authenticatedUser.getToken());
//        HttpEntity<Void> entity = new HttpEntity<>(headers);
//        try {
//            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL+"/details/"+id, HttpMethod.GET, makeAuthEntity(), Transfer.class);
//            transfer = response.getBody();
//        } catch (RestClientResponseException e) {
//            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
//        } catch (ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
//        }
//
//        return transfer;
//
//    }
//
//
//    //method to PUT an updated transfer to the server's endpoint http://localhost:8080/transfer/update
//    public void updateRequest(Transfer transfer) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(authenticatedUser.getToken());
//        HttpEntity<Transfer> entity = new HttpEntity<>(transfer,headers);
//        try {
//            restTemplate.put(API_BASE_URL + "/update", entity);
//
//        } catch (RestClientResponseException e) {
//            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
//        } catch (ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
//        }
//





    //makes the httpEntity with an authenticated token plus a transfer object
    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(transfer,headers);
    }




}
