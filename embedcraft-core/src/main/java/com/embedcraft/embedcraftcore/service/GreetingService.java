package com.embedcraft.embedcraftcore.service;

/**
 * A demo service interface for gRPC communication.
 */
public interface GreetingService {
    String sayHelloToServer(String msg);
}
