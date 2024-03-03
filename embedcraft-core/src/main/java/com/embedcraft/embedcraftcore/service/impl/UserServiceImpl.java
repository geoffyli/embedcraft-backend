package com.embedcraft.embedcraftcore.service.impl;

import com.embedcraft.embedcraftcore.entity.UserEntity;
import com.embedcraft.embedcraftcore.mapper.UserMapper;
import com.embedcraft.embedcraftcore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Implements the UserService interface to provide user management functionalities.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserMapper userMapper;

    /**
     * Constructs a UserServiceImpl with the specified UserMapper.
     *
     * @param userMapper the mapper for user data operations
     */
    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * User login
     *
     * @param account  the login name
     * @param password the password
     * @return the user ID if login is successful; -1 otherwise.
     *
     */
    @Override
    public Integer login(String account, String password) {

        if (strNullOrEmpty(account) || strNullOrEmpty(password))
            return -1;
        UserEntity userEntity = userMapper.selectUserByAccountAndPassword(account, password);
        // If the username does not exist, return -1
        return userEntity == null ? -1 : userEntity.getId();
    }


    /**
     * Adds a new user with the given account and password.
     *
     * @param account  the account for the new user
     * @param password the password for the new user
     * @return newly created user's ID if addition is successful; -1 otherwise
     */
    @Override
    public Integer addUser(String account, String password) {
        if (strNullOrEmpty(account) || strNullOrEmpty(password))
            return -1;
        if (userMapper.selectUserByAccount(account) != null) return -1; // Account already exists
        UserEntity userEntity = new UserEntity(account, password);
        if (userMapper.addUser(userEntity) > 0)
            return userEntity.getId();
        return -1;

    }

    /**
     * User logout
     *
     * @param token the JWT token to be invalidated
     */
    @Override
    public void logout(String token) {
        // TODO: Implementation details for invalidating JWT token

    }

    /**
     * Check if the input string is null or empty.
     *
     * @param str the string to be checked
     * @return if the string is null or empty
     */
    private static boolean strNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

}
