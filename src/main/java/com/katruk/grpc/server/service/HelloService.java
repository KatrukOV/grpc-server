package com.katruk.grpc.server.service;

import com.katruk.grpc.server.api.pb.Hello;
import com.katruk.grpc.server.domain.Random;
import com.katruk.grpc.server.domain.TimeOut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.katruk.grpc.server.api.HelloRpc.storeResult;

@Slf4j
@Service
@RequiredArgsConstructor
public class HelloService {

    public Hello.HelloResponse say(Hello.HelloRequest request) {
        String name = request.getName() + " | " + System.currentTimeMillis();
        String message;
        final int millisecond = new Random(5, 55).intInRange();
        new TimeOut(millisecond).waiting();
        if (millisecond % 7 == 0) {
            message = String.format("Something broke for %s in %d", name, millisecond);
            log.error(message);
            throw new RuntimeException(message);
        }

        message = String.format("Hello, %s !!! time: %s", name, millisecond);
        log.info(message);
        return Hello.HelloResponse.newBuilder()
                .setGreeting(message)
                .build();
    }

    public void revertSay(Hello.HelloRequest request) {
        storeResult.remove(request.getName());
        log.warn("-- {}", request.getName());
    }

}
