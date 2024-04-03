package com.embedcraft.embedcraftcore.service.impl;

import com.embedcraft.embedcraftcore.service.GreetingService;
import com.embedcraft.grpcclient.GrpcClient;
import org.springframework.stereotype.Service;

/**
 * A demo service class for basic gRPC communication.
 */
@Service
public class GreetingServiceImpl implements GreetingService {
    private final GrpcClient grpcClient = new GrpcClient();

    @Override
    public String sayHelloToServer(String msg) {
        return grpcClient.trainEmbeddings(msg);
    }
}
