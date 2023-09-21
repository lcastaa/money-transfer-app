package aqlabs.TenmoMoneyTransfer.xyz.exceptions.UserExceptions;

public class NullUsernameException extends Exception{

    public NullUsernameException(){
        super("Username Cannot Be Null");
    }


}
