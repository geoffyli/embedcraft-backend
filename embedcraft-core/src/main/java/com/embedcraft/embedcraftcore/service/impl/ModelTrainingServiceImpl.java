package com.embedcraft.embedcraftcore.service.impl;

import com.embedcraft.embedcraftcore.VO.TrainingSettingsVO;
import com.embedcraft.embedcraftcore.service.ModelTrainingService;
import com.embedcraft.embedcraftcore.util.GoogleCloudStorageUtil;
import com.embedcraft.grpcclient.GrpcClient;
import com.embedcraft.grpcclient.model.TrainRequestModel;
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
    /*
    Initialize the gRPC client.
     */
    private final GrpcClient grpcClient = new GrpcClient();

    /**
     * Create a bufferedReader of the uploaded dataset.
     * @param file the uploaded dataset file.
     */
    @Override
    public String uploadDatasetToCloud(MultipartFile file){
        try {
            byte[] bytes = file.getBytes();
            return GoogleCloudStorageUtil.uploadTextFile(bytes);
        } catch (Exception e) {
            // Handle the exception properly
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Submit the training settings to the python project
     * @param vo the view object that contains training settings
     * @return training task id on success
     */
    public String submitTrainingSettings(TrainingSettingsVO vo){
        /*
        Build a TrainRequestModel object.
         */
        TrainRequestModel trainRequestModel = new TrainRequestModel();
        trainRequestModel.setName(vo.getName());
        trainRequestModel.setTag(vo.getTag());
        trainRequestModel.setAlgorithm(vo.getAlgorithm());
        trainRequestModel.setEpochs(vo.getEpochs());
        trainRequestModel.setMinCount(vo.getMinCount());
        trainRequestModel.setVectorSize(vo.getVectorSize());
        trainRequestModel.setBlobName(vo.getBlobName());
        trainRequestModel.setWindowSize(vo.getWindowSize());
        return grpcClient.trainEmbeddings(trainRequestModel);
    }
}
