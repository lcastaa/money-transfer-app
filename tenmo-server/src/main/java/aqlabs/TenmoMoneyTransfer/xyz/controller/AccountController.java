package aqlabs.TenmoMoneyTransfer.xyz.controller;

import aqlabs.TenmoMoneyTransfer.xyz.dao.JdbcAccountDao;
import aqlabs.TenmoMoneyTransfer.xyz.exceptions.AccountExceptions.InvalidUserIdForAccountGetException;
import aqlabs.TenmoMoneyTransfer.xyz.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping(path = "/user/account")
public class AccountController {

    JdbcAccountDao dao;

    public AccountController(JdbcAccountDao dao) {
        this.dao = dao;
    }

    @GetMapping(path = "/id/{userId}")
    Account getAccountByUserId(@PathVariable Integer userId) throws InvalidUserIdForAccountGetException {
        return  dao.getAccountByUserId(userId);
    }

    @GetMapping(path = "/username/{username}")
    Account getAccountByUsername(@PathVariable String username){
        return dao.getAccountByUsername(username);
    }



}
