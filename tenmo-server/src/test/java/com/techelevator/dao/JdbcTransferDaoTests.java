package com.techelevator.dao;
import aqlabs.TenmoMoneyTransfer.xyz.dao.JdbcTransferDao;
import aqlabs.TenmoMoneyTransfer.xyz.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestingDatabaseConfig.class)
public class JdbcTransferDaoTests extends BaseDaoTests {

    protected static final Transfer TRANSFER_1 = new Transfer(3001, "Send", "Approved", 2002, 2001, new BigDecimal("15.00"),"Tenmo");
    protected static final Transfer TRANSFER_2 = new Transfer(3002, "Send", "Pending", 2002, 2001, new BigDecimal("25.00"),"Tenmo");
    private static final Transfer TRANSFER_3 = new Transfer(3003, "Send", "Approved", 2002, 2001, new BigDecimal("35.00"),"Tenmo");

    private JdbcTransferDao dao;
    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void getATransferByNullTransferIdReturnsNull(){
        Assert.assertNull(dao.getATransferByTransferId(null));
    }

    @Test
    public void getATransferByNegativeTransferIdReturnsNull(){
        Assert.assertEquals(null,dao.getATransferByTransferId(-1));
    }

    @Test
    public void getATransferByZeroTransferIdReturnsNull(){
        Assert.assertEquals(null,dao.getATransferByTransferId(0));
    }

    @Test
    public void getATransferByInvalidTransferIdReturnsNull(){
        Assert.assertEquals(null,dao.getATransferByTransferId(10));
        Assert.assertEquals(null,dao.getATransferByTransferId(2999));
    }

    @Test
    public void getATransferByCorrectTransferIdReturnsCorrectTransfer(){
        Transfer test = dao.getATransferByTransferId(3001);
        int transferId = test.getTransferId();
        String transferType = test.getTransferType();
        String transferStatus = test.getTransferStatus();
        int accountFrom = test.getAccountFrom();
        int accountTo = test.getAccountTo();
        BigDecimal amount = test.getAmount();
        //tests that the elements of the transfer are the same
        Assert.assertEquals(transferId,TRANSFER_1.getTransferId());
        Assert.assertEquals(transferType,TRANSFER_1.getTransferType());
        Assert.assertEquals(transferStatus,TRANSFER_1.getTransferStatus());
        Assert.assertEquals(accountFrom,TRANSFER_1.getAccountFrom());
        Assert.assertEquals(accountTo,TRANSFER_1.getAccountTo());
        Assert.assertEquals(amount.floatValue(),TRANSFER_1.getAmount().floatValue(),1);
    }

    @Test
    public void updateTransferStatusToApproved (){
        Transfer testTransferToCompareTo = dao.getATransferByTransferId(3002);
        testTransferToCompareTo.setTransferStatus("Approved");
        dao.updateTransferRecord(testTransferToCompareTo);
        Assert.assertEquals("Approved",dao.getATransferByTransferId(3002).getTransferStatus());
    }

    @Test
    public void updateTransferStatusToRejected(){
        Transfer testTransferToCompareTo = dao.getATransferByTransferId(3002);
        testTransferToCompareTo.setTransferStatus("Rejected");
        dao.updateTransferRecord(testTransferToCompareTo);
        Assert.assertEquals("Rejected",dao.getATransferByTransferId(3002).getTransferStatus());
    }



}

