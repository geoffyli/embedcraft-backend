package com.embedcraft.embedcraftcore.service.impl;

import com.embedcraft.embedcraftcore.VO.TrainingSettingsVO;
import com.embedcraft.embedcraftcore.entity.ModelEntity;
import com.embedcraft.embedcraftcore.mapper.ModelMapper;
import com.embedcraft.embedcraftcore.service.ModelTrainingService;
import com.embedcraft.embedcraftcore.util.GoogleCloudStorageUtil;
import com.embedcraft.grpcclient.GrpcClient;
import com.embedcraft.grpcclient.model.QueryResponseModel;
import com.embedcraft.grpcclient.model.TrainRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private final ModelMapper modelMapper;
    /*
    Initialize the gRPC client.
     */
    private final GrpcClient grpcClient = new GrpcClient();

    public ModelTrainingServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

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

    /**
     * Query the status of the training task
     *
     * @param taskId the task id to be queried with
     * @return the status of the task, 0: uncompleted, task id in MySQL: completed, -1: failed
     */
    @Override
    public Integer queryTrainingStatus(Integer userId, String taskId) {
        QueryResponseModel queryResponseModel= grpcClient.queryTrainingStatus(taskId);
        Integer status = queryResponseModel.getStatus();
        if (status == 0){
            // Return the stats
            return 0;
        }else{
            /*
             Save the records in MySQL
             */
            ModelEntity modelEntity = new ModelEntity();
            modelEntity.setModelFilePath(queryResponseModel.getModelFilePath());
            modelEntity.setModelId(taskId);
            modelEntity.setTrainingTime(queryResponseModel.getTrainingTime());
            modelEntity.setEpochs(queryResponseModel.getEpochs());
            modelEntity.setAlgorithm(queryResponseModel.getAlgorithm());
            modelEntity.setName(queryResponseModel.getName());
            modelEntity.setTag(queryResponseModel.getTag());
            modelEntity.setVectorSize(queryResponseModel.getVectorSize());
            modelEntity.setMinCount(queryResponseModel.getMinCount());
            modelEntity.setWindowSize(queryResponseModel.getWindowSize());
            modelEntity.setLossOverTime(queryResponseModel.getLossOverTime().toString());
            modelEntity.setVocabularySize(queryResponseModel.getVocabularySize());
            modelEntity.setUserId(userId);

            modelMapper.addModel(modelEntity);
            // TODO
            return 1;
        }

    }
}
