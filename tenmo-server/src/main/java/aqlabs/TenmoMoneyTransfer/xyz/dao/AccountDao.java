package aqlabs.TenmoMoneyTransfer.xyz.dao;

import aqlabs.TenmoMoneyTransfer.xyz.model.Account;

public interface AccountDao {

    Account getAccountByUserId(Integer userId);

    Account getAccountByUsername(String username);
}
