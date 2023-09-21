package aqlabs.TenmoMoneyTransfer.xyz.dao;

import aqlabs.TenmoMoneyTransfer.xyz.model.Transfer;
import aqlabs.TenmoMoneyTransfer.xyz.exceptions.TransferExceptions.InvalidTransferException;
import aqlabs.TenmoMoneyTransfer.xyz.exceptions.TransferExceptions.InvalidTransferIdException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;
    private final String SELECT = "SELECT transfer.transfer_id, transfer_type.transfer_type_desc, transfer_status.transfer_status_desc, transfer.account_from, transfer.account_to, transfer.amount, tenmo_user.username FROM transfer ";
    private final String JOIN = "JOIN account ON account.account_id = transfer.account_from JOIN tenmo_user ON tenmo_user.user_id = account.user_id JOIN transfer_status ON transfer_status.transfer_status_id = transfer.transfer_status_id JOIN transfer_type ON transfer_type.transfer_type_id = transfer.transfer_type_id ";
    private final String WHERE_TRANSFERID_IS = "where transfer.transfer_id = ?";
    private final String WHERE_USERID_IS = "where tenmo_user.user_id = ?";
    private final String WHERE_USERID_IS_AND_STATUS_IS = "where tenmo_user.user_id = ? and transfer.transfer_status_id = ?";
    private final String INSERT_TRANSFER = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES ((select transfer_type_id from transfer_type where transfer_type_desc = ?), (select transfer_status_id from transfer_status where transfer_status_desc = ?), ?, ?, ?)";
    private final String UPDATE_TRANSFER_STATS_TO_APPROVED = "UPDATE transfer SET transfer_status_id = 2 WHERE transfer_id = ?";
    private final String UPDATE_TRANSFER_STATS_TO_REJECTED = "UPDATE transfer SET transfer_status_id = 3 WHERE transfer_id = ?";
    private final String UPDATE_TRANSFER_STATS_TO_PENDING = "UPDATE transfer SET transfer_status_id = 1 WHERE transfer_id = ?";

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}


    @Override
    public Transfer getATransferByTransferId(Integer transferId) {
        Transfer transfer = new Transfer();
        try {
            //checks transferId for invalid values if found throw errors
            if (transferId == null || transferId >= 4000 || transferId <= 3001) {
                transfer = null;
                throw new InvalidTransferIdException();
                }
            } catch (InvalidTransferIdException e) {
                System.out.println(e.getMessage());
            }

            String sql = SELECT + JOIN + WHERE_TRANSFERID_IS;
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);
            if (result.next() && transfer != null) {
                transfer = mapRowToTransfer(result);
            }
        return transfer;
    }

    @Override
    public List<Transfer> getListOfSortedTransfersByUserId(Integer userId , Integer filter) {
        List<Transfer> transfers = new ArrayList<>();
        SqlRowSet result;
        String sql;
        if(filter != 4) {
            sql = SELECT + JOIN + WHERE_USERID_IS_AND_STATUS_IS;
            result = jdbcTemplate.queryForRowSet(sql, userId, filter);
        } else {
            sql = SELECT + JOIN + WHERE_USERID_IS;
            result = jdbcTemplate.queryForRowSet(sql, userId);
        }
        while (result.next()) {
            transfers.add(mapRowToTransfer(result));
        }
        return transfers;

    }


    public void transferFactory (Transfer transfer) {
        try {
            //checks for valid transfers if not catches error

            if (transfer.getTransferType().equals("Send") || transfer.getTransferType().equals("Request")) {
                if (transfer.getTransferStatus().equals("Approved") || transfer.getTransferStatus().equals("Rejected") || transfer.getTransferStatus().equals("Pending")) {
                        //checks if it is an PUT to an existing transfer
                        //before adding the transfer to the record process any Instant Transfers ie "Send, Approved"
                        if (transfer.getTransferType().equals("Send") && transfer.getTransferStatus().equals("Approved")) {
                            updateBalances(transfer);

                            //adds the transfer record to the database
                            recordTransfer(transfer);

                        //else if the transfer is other than Instant Transfer
                        }
                        else if (transfer.getTransferType().equals("Request") && transfer.getTransferStatus().equals("Approved")){
                           updateTransferRecord(transfer);
                           updateBalances(transfer);

                        } else {
                            //adds the transfer record to the database
                            recordTransfer(transfer);
                        }
                    } else {throw new InvalidTransferException();}
                } else {throw new InvalidTransferException();}
        } catch(InvalidTransferException e){
            System.out.println(e.getMessage());
        }
    }

    public void updateTransferRecord(Transfer transfer){
         updateTransfer(transfer);
    }



    //--------------------[Private methods for the Transfer Factory------------------------------



    //records transfer object to database method
    private void recordTransfer(Transfer transfer){
        jdbcTemplate.update(INSERT_TRANSFER,
                transfer.getTransferType(),
                transfer.getTransferStatus(),
                transfer.getAccountFrom(),
                transfer.getAccountTo(),
                transfer.getAmount());
    }

    //method to update balances after an approved transfer
    private void updateBalances(Transfer transfer){
        JdbcAccountDao accountDao = new JdbcAccountDao(jdbcTemplate);
        accountDao.addBalance(transfer.getAccountTo(), transfer.getAmount());
        accountDao.subtractBalance(transfer.getAccountFrom(), transfer.getAmount());
    }

    //method to update a transfer and process payment if approved
    private void updateTransfer(Transfer transfer) {
        int transferId = transfer.getTransferId();
        JdbcAccountDao accountDao = new JdbcAccountDao(jdbcTemplate);
        try {
            switch (transfer.getTransferStatus()) {
                case "Approved":
                    updateBalances(transfer);
                    jdbcTemplate.update(UPDATE_TRANSFER_STATS_TO_APPROVED, transferId);
                    break;

                case "Rejected":
                    jdbcTemplate.update(UPDATE_TRANSFER_STATS_TO_REJECTED, transferId);
                    break;
                case "Pending":
                    jdbcTemplate.update(UPDATE_TRANSFER_STATS_TO_PENDING ,transferId);
                    break;
                default: {
                    throw new InvalidTransferException();
                }
            }
        }catch (InvalidTransferException e){
            System.out.println(e.getMessage());
        }
    }

    //method to map elements to construct transfer object
    private Transfer mapRowToTransfer (SqlRowSet result){
        return new Transfer(
                result.getInt("transfer_id"),
                result.getString("transfer_type_desc"),
                result.getString("transfer_status_desc"),
                result.getInt("account_from"),
                result.getInt("account_to"),
                result.getBigDecimal("amount"),
                result.getString("username"));
    }
}
