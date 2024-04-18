package com.embedcraft.embedcraftcore.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ModelEntity implements Serializable {

    private String modelId;

    private Integer windowSize;

    private Integer userId;

    private String name;

    private String tag;

    private String algorithm;

    private Integer minCount;

    private Integer vectorSize;

    private Integer epochs;

    private Integer trainingTime;

    private Integer vocabularySize;

    private String modelFilePath;

    private String lossOverTime;
}
