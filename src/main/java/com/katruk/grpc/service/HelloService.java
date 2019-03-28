package com.katruk.grpc.service;

import com.katruk.grpc.api.pb.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HelloService {

    public Server.HelloResponse say(Server.HelloRequest request) {
        String name = request.getName();
        return Server.HelloResponse.newBuilder()
                .setGreeting(String.format("Hello, %s !!!", name))
                .build();
    }

}
