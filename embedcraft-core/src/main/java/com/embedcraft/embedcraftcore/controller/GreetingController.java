package com.embedcraft.embedcraftcore.controller;

import com.embedcraft.embedcraftcore.VO.GreetVO;
import com.embedcraft.embedcraftcore.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * A demo controller class for basic gRPC communication.
 */
@RestController
public class GreetingController {
    @Autowired
    private GreetingService greetingService;

    @PostMapping("/greet")
    public Map<String, Object> greet (@RequestBody GreetVO greetVO){
        String resMsg = greetingService.sayHelloToServer(greetVO.getData());
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("resMsg", resMsg);
        return responseBody;

    }

}
