package aqlabs.TenmoMoneyTransfer.xyz.exceptions.TransferExceptions;

public class InvalidTransferException extends Exception{

    public InvalidTransferException() {
        super("Transfer is not valid...!");
    }
}
