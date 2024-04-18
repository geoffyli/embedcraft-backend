package com.embedcraft.embedcraftcore.controller;

import com.embedcraft.embedcraftcore.entity.ModelEntity;
import com.embedcraft.embedcraftcore.service.ModelEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController

public class ModelEvaluationController {
    @Autowired
    private ModelEvaluationService modelEvaluationService;
    @GetMapping("model/evaluation")
    public ResponseEntity<Map<String, Object>> queryModelDetails(@RequestParam String modelId){
        Map<String, Object> responseBody = new HashMap<>();
        // Query training status
        ModelEntity modelEntity = modelEvaluationService.queryModelDetails(modelId);
        if (modelEntity == null){
            responseBody.put("message", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }else{
            // Convert loss over time into array
            String lossOverTimeStr = modelEntity.getLossOverTime();
            String[] stringNumbers = lossOverTimeStr.substring(1, lossOverTimeStr.length() - 1).split(",\\s*");
            double[] numbers = new double[stringNumbers.length];
            for (int i = 0; i < stringNumbers.length; i++) {
                numbers[i] = Double.parseDouble(stringNumbers[i]);
            }
            // Compose the response body
            responseBody.put("message", "success");
            responseBody.put("name", modelEntity.getName());
            responseBody.put("tag", modelEntity.getTag());
            responseBody.put("algorithm", modelEntity.getAlgorithm());
            responseBody.put("windowSize", modelEntity.getWindowSize());
            responseBody.put("vectorSize", modelEntity.getVectorSize());
            responseBody.put("minCount", modelEntity.getMinCount());
            responseBody.put("epochs", modelEntity.getEpochs());
            responseBody.put("trainingTime", modelEntity.getTrainingTime());
            responseBody.put("vocabularySize", modelEntity.getVocabularySize());
            responseBody.put("modelFilePath", modelEntity.getModelFilePath());
            responseBody.put("lossOverTime",numbers);
            return ResponseEntity.ok().body(responseBody);
        }
    }
}
