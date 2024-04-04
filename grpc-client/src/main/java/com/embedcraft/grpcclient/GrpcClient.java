package com.embedcraft.grpcclient;

import com.embedcraft.grpcclient.model.QueryResponseModel;
import com.embedcraft.grpcclient.model.TrainRequestModel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class performing as the gRPC client to interact with the sever based on the APIs defined in the proto buffer
 */
public class GrpcClient {
    // Logger for this class to log information and exceptions
    private static final Logger logger = Logger.getLogger(GrpcClient.class.getName());

    // Stubs for making gRPC calls - they abstract the remote method calls
    private ModelTrainingServiceGrpc.ModelTrainingServiceBlockingStub modelTrainingServiceBlockingStub;

    /**
     * Initialize the gRPC client by setting up the connection to the server.
     */
    private void initializeClient() {
        // Server address, typically read from configurations
        String serverAddress = "localhost:50051"; // This could be externalized to configuration
        // Create a channel for the server address without encryption (plaintext)
        // For development purposes; not recommended in production
        // Channel through which gRPC calls will be made
        ManagedChannel channel = ManagedChannelBuilder.forTarget(serverAddress)
                .usePlaintext() // For development purposes; not recommended in production
                .build();
        // Initialize blocking stubs for synchronous gRPC calls
        this.modelTrainingServiceBlockingStub = ModelTrainingServiceGrpc.newBlockingStub(channel);
    }


    {
        initializeClient();
        System.out.println("gRPC Client has been initialized");
    }

    /**
     * Method to call the trainEmbeddings RPC method provided by the Embeddings service
     *
     * @return the response msg from the server
     */
    public String trainEmbeddings(TrainRequestModel trainRequestModel) {
        // Prepare the request with dummy data for training
        TrainRequest request = TrainRequest.newBuilder().setName(trainRequestModel.getName())
                .setTag(trainRequestModel.getTag()).setMinCount(trainRequestModel.getMinCount())
                .setWindowSize(trainRequestModel.getWindowSize()).setEpochs(trainRequestModel.getEpochs())
                .setBlobName(trainRequestModel.getBlobName()).setAlgorithm(trainRequestModel.getAlgorithm())
                .setVectorSize(trainRequestModel.getVectorSize()).build();
        TrainResponse response; // Variable to store the response
        try {
            // Make the RPC call using the blocking stub
            response = modelTrainingServiceBlockingStub.trainModel(request);
        } catch (StatusRuntimeException e) {
            // Log and handle the exception if the RPC call fails
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return null; // Return null if there's an exception
        }
        // Log the received result message
        logger.info("Training settings Submission result: " + response.getMessage());
        return response.getTaskId(); // Return the task ID
    }

    public QueryResponseModel queryTrainingStatus(String taskId) {
        // Prepare the request
        StatusQueryRequest request = StatusQueryRequest.newBuilder().setTaskId(taskId).build();
        StatusQueryResponse response;
        try {
            response = modelTrainingServiceBlockingStub.queryTrainingStatus(request);
        } catch (StatusRuntimeException e) {
            // Log and handle the exception if the RPC call fails
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return null; // Return null if there's an exception
        }
        // Log the received result message
        logger.info("Task " + taskId + " status: " + response.getStatus());
        return new QueryResponseModel(response.getStatus(), response.getName(), response.getTag(),
                response.getAlgorithm(), response.getMinCount(), response.getVectorSize(), response.getWindowSize(),
                response.getEpochs(), response.getTrainingTime(), response.getVocabularySize(), response.getModelFilePath(),
                response.getLossOverTimeList());
    }

}
