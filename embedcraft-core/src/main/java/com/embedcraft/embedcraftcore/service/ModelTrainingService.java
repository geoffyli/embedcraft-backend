package com.embedcraft.embedcraftcore.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;

public interface ModelTrainingService {


    /**
     * Create a bufferedReader of the uploaded dataset.
     * @param file the uploaded dataset file.
     */
    String saveDatasetTemporarily(MultipartFile file);
    void trainModel(String filePath);
}
