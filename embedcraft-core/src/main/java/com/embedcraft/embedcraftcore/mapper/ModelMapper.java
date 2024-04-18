package com.embedcraft.embedcraftcore.mapper;

import com.embedcraft.embedcraftcore.entity.ModelEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

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
}
