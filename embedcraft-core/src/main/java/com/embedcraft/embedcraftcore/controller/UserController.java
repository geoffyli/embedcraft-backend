package com.embedcraft.embedcraftcore.controller;

import com.embedcraft.embedcraftcore.VO.UserInfoVO;
import com.embedcraft.embedcraftcore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Map<String, Object> login (@RequestBody UserInfoVO userInfo){
        Integer userID = userService.login(userInfo.getAccount(), userInfo.getPassword());
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("res", userID);
        // TODO: JWT Authorization
        // ...
        return responseBody;

    }
}
