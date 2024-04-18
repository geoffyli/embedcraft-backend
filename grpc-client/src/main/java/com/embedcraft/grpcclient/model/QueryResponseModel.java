package com.embedcraft.grpcclient.model;

import lombok.Data;

import java.util.List;

@Data
public class QueryResponseModel {
    private Integer status;
    private String name;
    private String tag;
    private String algorithm;
    private Integer minCount;
    private Integer vectorSize;
    private Integer windowSize;
    private Integer epochs;
    private Integer trainingTime;
    private Integer vocabularySize;
    private String modelFilePath;
    private List<Float> lossOverTime;

    public QueryResponseModel(int status, String name, String tag, String algorithm, int minCount, int vectorSize, int windowSize, int epochs, int trainingTime, int vocabularySize, String modelFilePath, List<Float> lossOverTimeList) {
        this.status = status;
        this.name = name;
        this.tag = tag;
        this.algorithm = algorithm;
        this.minCount = minCount;
        this.vectorSize = vectorSize;
        this.windowSize = windowSize;
        this.epochs = epochs;
        this.trainingTime = trainingTime;
        this.vocabularySize = vocabularySize;
        this.modelFilePath = modelFilePath;
        this.lossOverTime = lossOverTimeList;
    }
}
