package com.embedcraft.embedcraftcore.service;

import com.embedcraft.embedcraftcore.VO.TrainingSettingsVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;

public interface ModelTrainingService {


    /**
     * Create a bufferedReader of the uploaded dataset.
     * @param file the uploaded dataset file.
     */
    String uploadDatasetToCloud(MultipartFile file);

    /**
     * Submit the training settings to the python project
     * @param vo the view object that contains training settings
     * @return training task id on success
     */
    String submitTrainingSettings(TrainingSettingsVO vo);

    /**
     * Query the status of the training task
     * @param taskId the task id to be queried with
     * @return the status of the task, 0: uncompleted, 1: completed, -1: failed
     */
    Integer queryTrainingStatus(Integer userId, String taskId);

}
