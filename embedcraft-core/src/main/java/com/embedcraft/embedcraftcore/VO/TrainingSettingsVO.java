package com.embedcraft.embedcraftcore.VO;

import lombok.Data;


/**
 * VO object class for parsing the request body of model training API
 */
@Data
public class TrainingSettingsVO {
    private String name;
    private String tag;
    private String algorithm;
    private String blobName;
    private Integer minCount;
    private Integer vectorSize;
    private Integer windowSize;
    private Integer epochs;
}
