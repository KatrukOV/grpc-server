package com.katruk.grpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class GrpcApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        ApplicationContext ctx = SpringApplication.run(GrpcApplication.class, args);
        ServerRpc server = ctx.getBean(ServerRpc.class);
        server.start();
        server.awaitTermination();
    }

}
