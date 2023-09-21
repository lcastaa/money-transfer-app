package aqlabs.TenmoMoneyTransfer.xyz.services;


import aqlabs.TenmoMoneyTransfer.xyz.model.Transfer;
import aqlabs.TenmoMoneyTransfer.xyz.model.User;
import aqlabs.TenmoMoneyTransfer.xyz.model.UserCredentials;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);
    public final static String BORDER = "=======================================";

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }


    public void printGreeting() {
        System.out.println(BORDER);
        System.out.println("*           Welcome to TEnmo!         *");
        System.out.println(BORDER);
    }


    public void printLoginMenu() {
        System.out.println();
        System.out.println(" == [1] = Register");
        System.out.println(" == [2] = Login");
        System.out.println(" == [3] = Exit");
        System.out.println();
        System.out.println(BORDER);
    }


    public void printMainMenu(String username, BigDecimal balance) {
        System.out.println();
        System.out.println(BORDER);
        System.out.println("Hi, "+ username+"!"+ " Current Balance : $"+ balance);
        System.out.println(BORDER);
        System.out.println();
        System.out.println(" == [1] View your past transfers");
        System.out.println(" == [2] View your pending requests");
        System.out.println(" == [3] Send TE bucks");
        System.out.println(" == [4] Request TE bucks");
        System.out.println(" == [0] Exit");
        System.out.println();
        System.out.println(BORDER);
    }


    public UserCredentials promptForCredentials() {
        System.out.println();
        System.out.println("---------------------------");
        String username = promptForString("Enter [ Username ]: ");
        System.out.println("---------------------------");
        String password = promptForString("Enter [ Password ]: ");
        System.out.println("---------------------------");
        return new UserCredentials(username, password);
    }


    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }


    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }


    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }


    public void printPendingTransfersMenu(){
        System.out.println(BORDER);
        System.out.println("== [1] Approve Transfer");
        System.out.println("== [2] Reject Transfer");
        System.out.println("== [0] Exit ");
        System.out.println(BORDER);
    }




    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }


    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }




    //prints a transfer's details by passing in a transfer
    public void printViewTransferDetails(Transfer transfer){
        System.out.println(BORDER);
        System.out.println("             Transfer Details               ");
        System.out.println(BORDER);
        System.out.println("Id: " + transfer.getTransferId());
        System.out.println("From: " + transfer.getAccountFrom());
        System.out.println("To: " +transfer.getAccountTo());
        System.out.println("Type: " +transfer.getTransferType());
        System.out.println("Status: " +transfer.getTransferStatus());
        System.out.println("Amount: $" +transfer.getAmount());
    }

    public void printTransferMenu(){
        System.out.println(BORDER);
        System.out.println("== [1] List of Approved Transfers");
        System.out.println("== [2] List of Rejected Transfers");
        System.out.println("== [3] List of ALL Transfers");
        System.out.println("== [0] Exit ");
        System.out.println(BORDER);
    }

    public void printUsers(User user){
        System.out.println("TEnmo UserID : "+user.getId());
        System.out.println("TEnmo Username : "+user.getUsername());
        System.out.println(BORDER);
    }
}
