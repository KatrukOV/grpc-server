package com.katruk.grpc.server.service;

import com.katruk.grpc.server.api.pb.Hello;
import com.katruk.grpc.server.domain.Random;
import com.katruk.grpc.server.domain.TimeOut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HelloService {

    public Hello.HelloResponse say(Hello.HelloRequest request) {
        String name = request.getName();
        String message;
        final int millisecond = new Random(5, 55).intInRange();
        new TimeOut(millisecond).waiting();
        if (millisecond % 7 == 0) {
            throw new RuntimeException(String.format("Something broke for %s in %d", name, millisecond));
        }
        return Hello.HelloResponse.newBuilder()
                .setGreeting(String.format("Hello, %s !!! in %s", name, millisecond))
                .build();
    }

    public void revertSay(Hello.HelloRequest request) {
        log.warn("Clean up after rollback of Say to {}", request.getName());
    }

}
