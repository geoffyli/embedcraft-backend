package com.embedcraft.grpcclient;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.springframework.stereotype.Component;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class performing as the gRPC client to interact with the sever based on the APIs defined in the proto buffer
 */
public class GrpcClient {
    // Logger for this class to log information and exceptions
    private static final Logger logger = Logger.getLogger(GrpcClient.class.getName());

    // Stubs for making gRPC calls - they abstract the remote method calls
    private GreeterGrpc.GreeterBlockingStub greeterBlockingStub;
    private EmbeddingsServiceGrpc.EmbeddingsServiceBlockingStub embeddingsServiceBlockingStub;

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
        this.greeterBlockingStub = GreeterGrpc.newBlockingStub(channel);
        this.embeddingsServiceBlockingStub = EmbeddingsServiceGrpc.newBlockingStub(channel);
    }


    {
        initializeClient();
        System.out.println("Client has been initialized");
    }


    /**
     * Method to call the sayHello RPC method provided by the Greeter service
     * @param name the msg will be passed to the server
     * @return the response msg from the server
     */
    public String callSayHello(String name) {

        // Prepare the request with the provided name
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response; // Variable to store the response
        try{
            // Make the RPC call using the blocking stub
            response = greeterBlockingStub.sayHello(request);
        } catch (StatusRuntimeException e){
            // Log and handle the exception if the RPC call fails
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return null; // Return null if there's an exception
        }
        // Log the received response message
        logger.info("Received Greeting: " + response.getMessage());
        return response.getMessage(); // Return the response message
    }

    /**
     * Method to call the trainEmbeddings RPC method provided by the Embeddings service
     * @param data the msg will be passed to the server
     * @return the response msg from the server
     */
    public String trainEmbeddings(String data){
        // Prepare the request with dummy data for training
        TrainRequest request = TrainRequest.newBuilder().setData("Test Training").build();
        TrainResponse response; // Variable to store the response
        try{
            // Make the RPC call using the blocking stub
            response = embeddingsServiceBlockingStub.trainEmbeddings(request);
        }catch (StatusRuntimeException e){
            // Log and handle the exception if the RPC call fails
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return null; // Return null if there's an exception
        }
        // Log the received result message
        logger.info("Received result: " + response.getMessage());
        return response.getMessage(); // Return the result message
    }

}
