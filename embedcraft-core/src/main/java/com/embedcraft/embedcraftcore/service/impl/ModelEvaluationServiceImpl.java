package com.embedcraft.embedcraftcore.service.impl;

import com.embedcraft.embedcraftcore.entity.ModelEntity;
import com.embedcraft.embedcraftcore.mapper.ModelMapper;
import com.embedcraft.embedcraftcore.service.ModelEvaluationService;
import com.embedcraft.grpcclient.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ModelEvaluationServiceImpl implements ModelEvaluationService {
    @Autowired
    private final ModelMapper modelMapper;

    private final GrpcClient grpcClient = new GrpcClient();


    public ModelEvaluationServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ModelEntity queryModelDetails(String modelID) {
        ModelEntity modelEntity = modelMapper.queryModel(modelID);
        if (modelEntity != null)
            notifyModelLoading(modelEntity.getModelFilePath());
        return modelEntity;

    }

    @Override
    public Map<String, List<?>> getSimilarWordList(String modelId, String word) {
        ModelEntity modelEntity = modelMapper.queryModel(modelId);
        return grpcClient.querySimilarWordList(modelEntity.getModelFilePath(), word);
    }

    @Override
    public List<ModelEntity> getListOfModels(Integer userId, String name, String tag) {
        return modelMapper.queryModelList(userId, name, tag);
    }

    private void notifyModelLoading(String modelFilePath){
        grpcClient.notifyModelLoading(modelFilePath);
    }
}
