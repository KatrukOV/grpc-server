package com.katruk.grpc.server;

import com.katruk.grpc.server.api.HelloRpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ServerRpc {

    private final Server server;

    public ServerRpc(HelloRpc helloRpc) {
        this.server = ServerBuilder.forPort(8011)
                .addService(helloRpc)
                .build();
    }

    void start() throws IOException {
        server.start();
    }

    void awaitTermination() throws InterruptedException {
        server.awaitTermination();
    }

}
