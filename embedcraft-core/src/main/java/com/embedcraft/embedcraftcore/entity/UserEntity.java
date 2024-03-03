package com.embedcraft.embedcraftcore.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class UserEntity implements Serializable {

    public UserEntity(String account, String password){
        this.account = account;
        this.password = password;
    }

    private Integer id;

    private String account;

    private String password;

}
