package aqlabs.TenmoMoneyTransfer.xyz.Exceptions;

public class BalanceIsInsufficientException extends Exception{
    public BalanceIsInsufficientException() {
        super("You do not have enough funds to process transaction...!");
    }
}
