package aqlabs.TenmoMoneyTransfer.xyz.dao;

import aqlabs.TenmoMoneyTransfer.xyz.model.User;

import java.util.List;

public interface UserDao {

    //------------------------------------------------------------

    List<User> findAll();

    User getUserById(int id);

    User findByUsername(String username);


    //-----------------------------------------------------------

    int findIdByUsername(String username);

    boolean create(String username, String password);

    //experimental
    //boolean delete(String username, String password);
}
