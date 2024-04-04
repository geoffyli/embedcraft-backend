package com.embedcraft.embedcraftcore.controller;


import com.embedcraft.embedcraftcore.VO.TrainingSettingsVO;
import com.embedcraft.embedcraftcore.service.ModelTrainingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for handling model training operations.
 */
@RestController
public class ModelTrainingController {
    @Autowired
    private ModelTrainingService modelTrainingService;

    /**
     * Handles the uploading of a dataset file to cloud storage.
     *
     * @param file The multipart file uploaded by the client.
     * @return ResponseEntity with status and response information.
     * @throws IOException if there's an error processing the file.
     */
    @PostMapping("model/upload")
    public ResponseEntity<Map<String, Object>> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
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
        // Get the file and save it somewhere
        String blobName = modelTrainingService.uploadDatasetToCloud(file);
        if (blobName != null){
            responseBody.put("blobName", blobName);
            return ResponseEntity.ok().body(responseBody);
        }else{
            responseBody.put("error", "Could not process the file");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    /**
     * Endpoint to initiate the training of a model with provided settings.
     *
     * @param trainingSettingsVO The settings for the model training.
     * @return success message and taskId if the training is initialized successfully
     */
    @PostMapping("model/train")
    public ResponseEntity<Map<String, Object>> trainModel(@RequestBody TrainingSettingsVO trainingSettingsVO) {
        Map<String, Object> responseBody = new HashMap<>();
        String res = modelTrainingService.submitTrainingSettings(trainingSettingsVO);
        if (res == null || res.equals("error")){
            responseBody.put("message", "Error: Could not process the file");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }else{
            responseBody.put("message", "success");
            responseBody.put("taskId", res);
            return ResponseEntity.ok().body(responseBody);
        }
    }

    @GetMapping("model/status")
    public ResponseEntity<Map<String, Object>> queryTrainingStatus(@RequestParam String taskId, @RequestParam Integer userId){
        Map<String, Object> responseBody = new HashMap<>();
        // Query training status
        Integer res = modelTrainingService.queryTrainingStatus(userId, taskId);
        responseBody.put("status", res);
        return ResponseEntity.ok().body(responseBody);
    }


}
