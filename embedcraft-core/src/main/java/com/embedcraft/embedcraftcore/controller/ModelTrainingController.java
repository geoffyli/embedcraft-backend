package com.embedcraft.embedcraftcore.controller;


import com.embedcraft.embedcraftcore.service.ModelTrainingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ModelTrainingController {
    @Autowired
    private ModelTrainingService modelTrainingService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> handleFileUpload(@RequestParam("file") MultipartFile file) {
        Map<String, Object> responseBody = new HashMap<>();

        // Check if the file is empty or not
        if (file.isEmpty()) {
            responseBody.put("error", "File is empty.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        }
        // Check the file's size and throw exception if it exceeds the limit
        // Example: If the file size exceeds 1GB, throw exception
        if (file.getSize() > (1024 * 1024 * 1024)) {
            responseBody.put("error", "File is too large.");
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(responseBody);
        }
        String filePath = modelTrainingService.saveDatasetTemporarily(file);
        if (filePath != null){
            responseBody.put("filePath", filePath);
            return ResponseEntity.ok().body(responseBody);
        }else{
            responseBody.put("error", "Could not process the file");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }
}
