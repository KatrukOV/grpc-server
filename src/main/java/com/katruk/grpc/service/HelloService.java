package com.katruk.grpc.service;

import com.katruk.grpc.api.pb.Hello;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HelloService {

    public Hello.HelloResponse say(Hello.HelloRequest request) {
        String name = request.getName();
        return Hello.HelloResponse.newBuilder()
                .setGreeting(String.format("Hello, %s !!!", name))
                .build();
    }

}
