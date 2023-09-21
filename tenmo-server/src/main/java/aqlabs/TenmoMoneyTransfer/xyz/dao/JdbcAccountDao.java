package aqlabs.TenmoMoneyTransfer.xyz.dao;

import aqlabs.TenmoMoneyTransfer.xyz.model.Account;
import aqlabs.TenmoMoneyTransfer.xyz.exceptions.AccountExceptions.InvalidUserIdForAccountGetException;
import aqlabs.TenmoMoneyTransfer.xyz.exceptions.UserExceptions.NullUsernameException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;
    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final String GET_ACCOUNT_USING_USERID = "Select account_id, balance, account.user_id from account join tenmo_user ON tenmo_user.user_id = account.user_id where tenmo_user.user_id = ?";
    private final String GET_ACCOUNT_USING_USERNAME = "Select account_id, balance, account.user_id from account join tenmo_user ON tenmo_user.user_id = account.user_id where tenmo_user.username = ?";
    private final String SUBTRACT_FROM_USING_ACCOUNTID = "UPDATE account SET balance = (SELECT balance FROM account WHERE account_id = ?) - ? WHERE account_id = ?;";
    private final String ADD_TO_USING_ACCOUNTID =  "UPDATE account SET balance = (SELECT balance FROM account WHERE account_id = ?) + ? WHERE account_id = ?;";

    //---------------------------[Core Methods for getting Account Objects]---------------------------------------------

    // ** method to get an account by userId **
    @Override
    public Account getAccountByUserId(Integer userId){
        Account account = new Account();
        try{
            //checks for null or negative or zero values
            if(userId == null){
                account = null;
                throw new InvalidUserIdForAccountGetException("UserId cannot be null...!");
            }else if(userId <= 1000 || userId >= 1999){
                account = null;
                throw new InvalidUserIdForAccountGetException("UserId is invalid...!");
            }
            //retrieves the account object using the userId
            SqlRowSet result = jdbcTemplate.queryForRowSet(GET_ACCOUNT_USING_USERID,userId);
            if(result.next()){
                account = mapRowToAccount(result);
            }
        // catches errors from above
        }catch (InvalidUserIdForAccountGetException e){

        }
        return account;
    }

    // *** Method to get Account by username
    @Override
    public Account getAccountByUsername(String username) {
        Account account = new Account();
        try{

            //checks for null values
            if(username == null){
                throw new NullUsernameException();
            }
            //retrieves an Account object using the username
            SqlRowSet result = jdbcTemplate.queryForRowSet(GET_ACCOUNT_USING_USERNAME,username);
            if(result.next()){
                account = mapRowToAccount(result);
            }
            // catches errors from above
        }catch (NullUsernameException e){
            System.out.println(e.getMessage());
        }
        return account;
    }

    //method to subtract money from a user's Account
    public void subtractBalance (int accountId, BigDecimal amount){
        jdbcTemplate.update(SUBTRACT_FROM_USING_ACCOUNTID, accountId, amount, accountId);
    }


    //method to add money to a user's Account
    public void addBalance (int accountId, BigDecimal amount){
        jdbcTemplate.update(ADD_TO_USING_ACCOUNTID, accountId, amount, accountId);
    }



    //-------------------------------------[Private Methods]----------------------------------------


    //method to map result to construct an Account object
    private Account mapRowToAccount(SqlRowSet rs) {
        return new Account(
                    rs.getInt("account_id"),
                    rs.getInt("user_id"),
                    rs.getBigDecimal("balance"));
    }
}
