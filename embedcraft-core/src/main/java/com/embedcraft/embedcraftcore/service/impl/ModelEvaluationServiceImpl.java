package com.embedcraft.embedcraftcore.service.impl;

import com.embedcraft.embedcraftcore.entity.ModelEntity;
import com.embedcraft.embedcraftcore.mapper.ModelMapper;
import com.embedcraft.embedcraftcore.service.ModelEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModelEvaluationServiceImpl implements ModelEvaluationService {
    @Autowired
    private final ModelMapper modelMapper;

    public ModelEvaluationServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ModelEntity queryModelDetails(String modelID) {
        return modelMapper.queryModel(modelID);

    }
}
