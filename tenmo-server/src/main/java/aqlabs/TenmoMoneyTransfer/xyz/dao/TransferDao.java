package aqlabs.TenmoMoneyTransfer.xyz.dao;

import aqlabs.TenmoMoneyTransfer.xyz.model.Transfer;

import java.util.List;

public interface TransferDao {

    Transfer getATransferByTransferId(Integer transferId);

    List<Transfer> getListOfSortedTransfersByUserId(Integer userId , Integer filter);
    void transferFactory(Transfer transfer);


}
