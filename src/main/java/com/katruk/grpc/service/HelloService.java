package com.katruk.grpc.service;

import com.katruk.grpc.api.pb.Hello;
import com.katruk.grpc.domain.TimeOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.katruk.grpc.domain.TimeOut.waiting;

@Service
@RequiredArgsConstructor
public class HelloService {

    public Hello.HelloResponse say(Hello.HelloRequest request) {
        String name = request.getName();
        waiting(3);
        return Hello.HelloResponse.newBuilder()
                .setGreeting(String.format("Hello, %s !!!", name))
                .build();
    }

}
