package aqlabs.TenmoMoneyTransfer.xyz.exceptions.UserExceptions;

public class EmptyUsernameException extends Exception{

    public EmptyUsernameException() {
        super("Username Cannot be EMPTY..!");
    }
}
