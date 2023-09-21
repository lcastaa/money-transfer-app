package aqlabs.TenmoMoneyTransfer.xyz;

import aqlabs.TenmoMoneyTransfer.xyz.Exceptions.BalanceIsInsufficientException;
import aqlabs.TenmoMoneyTransfer.xyz.Exceptions.SendTransferInputInvalid;
import aqlabs.TenmoMoneyTransfer.xyz.model.AuthenticatedUser;
import aqlabs.TenmoMoneyTransfer.xyz.model.Transfer;
import aqlabs.TenmoMoneyTransfer.xyz.model.User;
import aqlabs.TenmoMoneyTransfer.xyz.model.UserCredentials;
import aqlabs.TenmoMoneyTransfer.xyz.services.*;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            AccountService accountService = new AccountService(currentUser);
            consoleService.printMainMenu(currentUser.getUser().getUsername() ,accountService.getAccount(currentUser.getUser().getId()).getBalance());
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");

            if (menuSelection == 1) {
                viewTransferHistory();
            } else if (menuSelection == 2) {
                viewPendingRequests();
            } else if (menuSelection == 3) {
                sendBucks();
            } else if (menuSelection == 4) {
                requestBucks();
            }  else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    //method to view a list of Approved Transfers and allows you to view the details of the transfer
    private void viewTransferHistory() {
        int choice = -1;
        Transfer[] transfers = {};
        while(choice != 0){
            consoleService.printTransferMenu();
            choice = consoleService.promptForInt("Please choose a option: ");
            switch (choice){
                case 1 :
                    transfers = getTransferArr(currentUser,2);
                    break;
                case 2 :
                    transfers = getTransferArr(currentUser,3);
                    break;
                case 3 :
                    transfers = getTransferArr(currentUser,4);
                    break;
            }
            choice = 0;
            for (Transfer transfer : transfers){
                consoleService.printViewTransferDetails(transfer);
            }
        }
    }


    //printing out pending requests
    private void viewPendingRequests() {
        Transfer tempTransfer = new Transfer();
        TransferService transferService = new TransferService(currentUser);
        int pendingListSize = transferService.getTransfers(currentUser.getUser().getId(), 1).length;
        if(pendingListSize > 0){
            for (Transfer transfer : transferService.getTransfers(currentUser.getUser().getId(), 1)){
                consoleService.printViewTransferDetails(transfer);
            }
            consoleService.printPendingTransfersMenu();
            int usrChoice = consoleService.promptForInt("Please choose an option: ");
            if(usrChoice == 1) {
                tempTransfer = transferService.getATransfer(consoleService.promptForInt("Please enter the transfer ID: "));
                transferService.sendUpdatedRequest(approveOrRejectRequest(tempTransfer,"Approved"));
                System.out.println("Request has been approved..!");
            } else if (usrChoice == 2){
                tempTransfer = transferService.getATransfer(consoleService.promptForInt("Please enter the transfer ID: "));
                transferService.sendUpdatedRequest(approveOrRejectRequest(tempTransfer,"Rejected"));
                System.out.println("Request has been rejected..!");
            }
        }else {
            System.out.println("There is no pending Requests");
        }
    }

    //method prints out accounts to send to and displays who you can send to
    private void sendBucks(){
        UserService userService = new UserService(currentUser);
        AccountService accountService = new AccountService(currentUser);
        TransferService transferService = new TransferService(currentUser);
        System.out.println(ConsoleService.BORDER);
        for (User user : userService.getAllUsers()){
            if(user.getId() == currentUser.getUser().getId()){
                continue;
            }
            consoleService.printUsers(user);
        }
        //ensure validation of transfer
        try{
            String type = "Send";
            String status = "Approved";
            int accF = accountService.getAccount(currentUser.getUser().getId()).getAccountId();
            int accT = accountService.getAccount(consoleService.promptForInt("Enter userId you want to pay: ")).getAccountId();
            if(accT >= 3000){
                throw new SendTransferInputInvalid();
            }
            BigDecimal amount = consoleService.promptForBigDecimal("Please Enter amount: $");
            if(amount.doubleValue() > accountService.getAccount(currentUser.getUser().getId()).getBalance().doubleValue()){
                throw new BalanceIsInsufficientException();
            }
            String username = currentUser.getUser().getUsername();
            transferService.sendInstant(contructTransfer(type, status, accF, accT, amount, username));

        } catch (SendTransferInputInvalid e){
            System.out.println(e.getMessage());
        } catch (BalanceIsInsufficientException e){
            System.out.println(e.getMessage());
        }
        System.out.println();
        System.out.println("PAYMENT SUCCESSFULL...!");
    }

    //method allows you to request Bucks from another user
    private void requestBucks(){
        UserService userService = new UserService(currentUser);
        AccountService accountService = new AccountService(currentUser);
        TransferService transferService = new TransferService(currentUser);
        System.out.println(ConsoleService.BORDER);
        for (User user : userService.getAllUsers()){
            if(user.getId() == currentUser.getUser().getId()){
                continue;
            }
            consoleService.printUsers(user);
        }
        try{
            String type = "Request";
            String status = "Pending";
            int accF = accountService.getAccount(consoleService.promptForInt("Enter Id of who you are requesting: ")).getAccountId();
            int accT = accountService.getAccount(currentUser.getUser().getId()).getAccountId();
            if(accT >= 3000){
                throw new SendTransferInputInvalid();
            }
            BigDecimal amount = consoleService.promptForBigDecimal("Please Enter amount to be requested: $");
            String username = currentUser.getUser().getUsername();
            transferService.sendRequest(contructTransfer(type, status, accF, accT, amount, username));

        } catch (SendTransferInputInvalid e){
            System.out.println(e.getMessage());
        }
        System.out.println();
        System.out.println("REQUEST SENT SUCCESSFULLY...!");
    }

    private Transfer approveOrRejectRequest(Transfer transfer, String status) {
        transfer.setTransferStatus(status);
        return  transfer;
    }

    private Transfer[] getTransferArr(AuthenticatedUser currentUser, int filter){
        TransferService transferService = new TransferService(currentUser);
        return transferService.getTransfers(currentUser.getUser().getId(),filter);
    }
    private Transfer contructTransfer(String type, String status, int accF, int accT, BigDecimal amount, String username){
        Transfer transfer = new Transfer();
        transfer.setTransferType(type);
        transfer.setTransferStatus(status);
        transfer.setAccountFrom(accF);
        transfer.setAccountTo(accT);
        transfer.setAmount(amount);
        transfer.setUsername(username);
        return  transfer;
    }
}
