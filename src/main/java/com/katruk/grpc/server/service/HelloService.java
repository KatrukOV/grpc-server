package com.katruk.grpc.server.service;

import com.katruk.grpc.server.api.pb.Hello;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.katruk.grpc.server.domain.TimeOut.waiting;

@Service
@RequiredArgsConstructor
public class HelloService {

    public Hello.HelloResponse say(Hello.HelloRequest request) {
        String name = request.getName();
        waiting(5, 1700);
        return Hello.HelloResponse.newBuilder()
                .setGreeting(String.format("Hello, %s !!!", name))
                .build();
    }

}
