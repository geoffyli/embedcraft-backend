package com.embedcraft.embedcraftcore.service;

import com.embedcraft.embedcraftcore.entity.ModelEntity;

import java.util.List;
import java.util.Map;

public interface ModelEvaluationService {
    ModelEntity queryModelDetails(String modelID);
    Map<String, List<?>> getSimilarWordList(String modelId, String word);

    List<ModelEntity> getListOfModels(Integer userId, String name, String tag);

}
