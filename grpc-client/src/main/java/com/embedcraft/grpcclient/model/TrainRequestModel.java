package com.embedcraft.grpcclient.model;

import lombok.Data;

@Data
public class TrainRequestModel {
    private String name;
    private String tag;
    private String algorithm;
    private String blobName;
    private Integer minCount;
    private Integer vectorSize;
    private Integer windowSize;
    private Integer epochs;
}
