package com.embedcraft.embedcraftcore.VO;

import lombok.Data;

/**
 * Demo view object class for parsing the request body of user login api.
 */
@Data
public class UserInfoVO {
    private String account;
    private String password;
}
