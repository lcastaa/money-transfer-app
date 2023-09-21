package com.techelevator.dao;

import aqlabs.TenmoMoneyTransfer.xyz.dao.JdbcAccountDao;
import aqlabs.TenmoMoneyTransfer.xyz.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class JdbcAccountDaoTests extends BaseDaoTests{
    protected static final Account ACCOUNT_1 = new Account(2001, 1001, BigDecimal.valueOf(100.00));

    private JdbcAccountDao testDao;

    @Before
    public void setup(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        testDao = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void negativeAsTheAccountIdReturnsNull(){
        Assert.assertEquals(null, testDao.getAccountByUserId(null));
    }

    @Test
    public void zeroAsTheAccountIdReturnsNull(){
        Assert.assertEquals(null, testDao.getAccountByUserId(0));
    }

    @Test
    public void wrongIdReturnsNull() {
       Assert.assertEquals(null, testDao.getAccountByUserId(12));
    }

    @Test
    public void correctIdReturnsCorrectAccount(){
        int accountId = testDao.getAccountByUserId(1001).getAccountId();
        int userId = testDao.getAccountByUserId(1001).getUserId();
        Assert.assertEquals(accountId, ACCOUNT_1.getAccountId());
        Assert.assertEquals(userId, ACCOUNT_1.getUserId());
        Assert.assertEquals(testDao.getAccountByUserId(1001).getBalance().floatValue(), ACCOUNT_1.getBalance().floatValue(),1);
    }

    @Test
    public void addBalance_adds_appropriate_amount(){
        testDao.addBalance(2001, BigDecimal.valueOf(50));
        Account test = testDao.getAccountByUserId(1001);
        BigDecimal result = test.getBalance();
        Assert.assertEquals(0, BigDecimal.valueOf(150.00).compareTo(result));
    }


    @Test
    public void subtractBalance_subtracts_appropriate_amount(){
        testDao.subtractBalance(2001, BigDecimal.valueOf(50));
        Account test = testDao.getAccountByUserId(1001);
        BigDecimal result = test.getBalance();
        Assert.assertEquals(0, BigDecimal.valueOf(50.00).compareTo(result));
    }
    }
