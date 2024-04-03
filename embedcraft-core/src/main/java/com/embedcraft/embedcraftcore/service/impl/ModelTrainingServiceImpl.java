package com.embedcraft.embedcraftcore.service.impl;

import com.embedcraft.embedcraftcore.service.ModelTrainingService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;


@Service
public class ModelTrainingServiceImpl implements ModelTrainingService {

    @Override
    public String saveDatasetTemporarily(MultipartFile file) {
        try {
            // Create a temporary file
            File tempFile = File.createTempFile("dataset", ".tmp");
            tempFile.deleteOnExit(); // Request deletion of the temp file on JVM exit

            // Copy the contents of the MultipartFile to the temporary file
            Files.copy(file.getInputStream(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            return tempFile.getAbsolutePath();
        } catch (Exception e) {
            // Handle the exception properly
            return null;
        }
    }

    public void trainModel(String filePath) {
//        if (this.tempFilePath != null && !this.tempFilePath.isEmpty()) {
//            try (Stream<String> stream = Files.lines(Paths.get(this.tempFilePath))) {
//                stream.forEach(line -> {
//                    // Process each line for model training
//                });
//            } catch (IOException e) {
//                // Handle the exception properly
//                e.printStackTrace();
//            }
//        }
    }
}
