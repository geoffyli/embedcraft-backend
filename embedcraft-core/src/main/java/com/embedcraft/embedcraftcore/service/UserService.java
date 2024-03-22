package com.embedcraft.embedcraftcore.service;

import com.embedcraft.embedcraftcore.entity.UserEntity;

public interface UserService {
    /**
     * User login
     * @param account the login name
     * @param password the password
     * @return the user id
     */
    Integer login(String account,String password);

    /**
     * Add user
     * @param account the account
     * @param password the password
     * @return the user id
     */
    Integer addUser(String account, String password);
}
