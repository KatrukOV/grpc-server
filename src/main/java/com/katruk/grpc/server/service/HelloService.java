package com.katruk.grpc.server.service;

import com.katruk.grpc.server.api.pb.Hello;
import com.katruk.grpc.server.domain.Random;
import com.katruk.grpc.server.domain.TimeOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class HelloService {

    public Hello.HelloResponse say(Hello.HelloRequest request) {
        String name = request.getName();
        final int millisecond = new Random(5, 55).intInRange();
        new TimeOut(millisecond).waiting();
        if (millisecond % 7 == 0) {
            throw new RuntimeException("Something broke for " + name + " in " + millisecond);
        }
        return Hello.HelloResponse.newBuilder()
                .setGreeting(String.format("Hello, %s !!! in %s", name, millisecond))
                .build();
    }

}
