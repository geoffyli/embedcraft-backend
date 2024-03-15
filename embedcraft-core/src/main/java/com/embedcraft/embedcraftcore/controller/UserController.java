package com.embedcraft.embedcraftcore.controller;

import com.embedcraft.embedcraftcore.VO.UserInfoVO;
import com.embedcraft.embedcraftcore.service.UserService;
import com.embedcraft.embedcraftcore.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;


import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserInfoVO userInfo) {
        Map<String, Object> responseBody = new HashMap<>();

        Integer userID = userService.login(userInfo.getAccount(), userInfo.getPassword());
        // If login failed (userID < 0)
        if (userID < 0) {
            responseBody.put("error", "Invalid login credentials.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }
        responseBody.put("userId", userID);
        // TODO: JWT Authorization
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", JWTUtil.createJWT(userID));

        return ResponseEntity.ok().headers(responseHeaders).body(responseBody);
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> addUser(@RequestBody UserInfoVO userInfo) {
        Map<String, Object> responseBody = new HashMap<>();
        Integer userID = userService.addUser(userInfo.getAccount(), userInfo.getPassword());
        //If register failed
        if (userID < 0) {
            responseBody.put("error", "Account already exists or invalid registration.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }
        // Successful registration
        responseBody.put("userId", userID);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", JWTUtil.createJWT(userID));

        return ResponseEntity.ok().headers(responseHeaders).body(responseBody);

        // return ResponseEntity.ok(responseBody);

    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader(value = "Authorization", required = false) String Token) {
        Map<String, Object> responseBody = new HashMap<>();

        // Verify that the token exists
        if (Token != null && !Token.isEmpty()) {
            // add a token to the blacklist.
            JWTUtil.invalidateJWT(Token);

            responseBody.put("message", "Successfully logged out.");
            return ResponseEntity.ok().body(responseBody);
        } else {
            // Request Failed
            responseBody.put("error", "Missing or invalid Authorization header.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }
    }





}
