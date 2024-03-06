package com.embedcraft.embedcraftcore.controller;

import com.embedcraft.embedcraftcore.VO.UserInfoVO;
import com.embedcraft.embedcraftcore.service.UserService;
import com.embedcraft.embedcraftcore.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;


import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login (@RequestBody UserInfoVO userInfo){
        Map<String, Object> responseBody = new HashMap<>();

        Integer userID = userService.login(userInfo.getAccount(), userInfo.getPassword());
        // If login failed (userID < 0)
        if (userID < 0){
            responseBody.put("error", "Invalid login credentials.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(responseBody);
        }
        responseBody.put("userId", userID);
        // TODO: JWT Authorization
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", JWTUtil.createJWT(userID));

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(responseBody);

    }
}
