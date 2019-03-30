package com.katruk.grpc.server.api;

import com.katruk.grpc.server.api.pb.Hello;
import com.katruk.grpc.server.api.pb.HelloApiGrpc;
import com.katruk.grpc.server.service.HelloService;
import io.grpc.Context;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

import static io.grpc.Status.CANCELLED;
import static java.time.LocalDateTime.now;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelloRpc extends HelloApiGrpc.HelloApiImplBase {

    private final HelloService helloService;

    @Override
    public void say(Hello.HelloRequest request, StreamObserver<Hello.HelloResponse> response) {
        LocalDateTime start = now();
        try {
            Hello.HelloResponse result = this.helloService.say(request);
            if (Context.current().isCancelled()) {
                response.onError(CANCELLED
                        .withDescription("Call CANCELLED")
                        .asRuntimeException()
                );
                log.warn("Call cancelled by client!");
            } else {
                response.onNext(result);
                response.onCompleted();
                log.info("Res = {}. TimeOF: {}", result.getGreeting(), Duration.between(start, now()));
            }
        } catch (Exception ex) {
            response.onError(ex);
            log.error("Error {}", ex.getMessage());
        }
    }

}