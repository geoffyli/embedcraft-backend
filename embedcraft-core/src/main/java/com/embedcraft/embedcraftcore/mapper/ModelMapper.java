package com.embedcraft.embedcraftcore.mapper;

import com.embedcraft.embedcraftcore.entity.ModelEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * Mapper interface for executing SQL operations on the model entity.
 */
@Mapper
public interface ModelMapper {
    /**
     * Inserts a new model into the database.
     *
     * @param modelEntity the ModelEntity object containing the data of the model to add
     * @return the number of rows affected by the insert operation
     */
    @Insert("INSERT INTO model(model_id, window_size, user_id, name, tag, algorithm, min_count, vector_size, epochs, training_time, vocabulary_size, model_file_path, loss_over_time) VALUES(#{modelId}, #{windowSize}, #{userId}, #{name}, #{tag}, #{algorithm}, #{minCount}, #{vectorSize}, #{epochs}, #{trainingTime}, #{vocabularySize}, #{modelFilePath}, #{lossOverTime})")
    @Options(keyProperty = "model_id")
    int addModel(ModelEntity modelEntity);

    /**
     * Queries the database for a model with the given modelId.
     *
     * @param modelId the ID of the model to query
     * @return the ModelEntity object representing the model data, or null if not found
     */
    @Select("SELECT * FROM model WHERE model_id = #{modelId}")
    ModelEntity queryModel(String modelId);
}
