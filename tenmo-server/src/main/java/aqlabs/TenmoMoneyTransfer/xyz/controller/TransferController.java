package aqlabs.TenmoMoneyTransfer.xyz.controller;


import aqlabs.TenmoMoneyTransfer.xyz.dao.JdbcTransferDao;
import aqlabs.TenmoMoneyTransfer.xyz.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/transfer")
public class TransferController {
    JdbcTransferDao dao;

    public TransferController(JdbcTransferDao dao) {
        this.dao = dao;
    }

    @GetMapping(path = "/{transactionId}")
    Transfer getTransferByTransferId(@PathVariable Integer transactionId){
        return dao.getATransferByTransferId(transactionId);
    }

    //returns unfiltered List of transfers fetched with userId
    @GetMapping(path = "/list/id/{userId}/{filter}")
    List<Transfer> getListOfSortedTransfersByUserId(@PathVariable Integer userId, @PathVariable Integer filter){
        return dao.getListOfSortedTransfersByUserId(userId, filter);
    }

    //Processes a transfer POST (New)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/send")
    void processTransfer(@RequestBody Transfer transfer) {
        dao.transferFactory(transfer);
    }

    //Processes a transfer PUT (Update)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping(path = "/update")
    void processUpdatedTransfer(@RequestBody Transfer transfer){
        dao.updateTransferRecord(transfer);
    }




}
