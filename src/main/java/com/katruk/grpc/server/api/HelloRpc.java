package com.katruk.grpc.server.api;

import com.katruk.grpc.server.api.pb.Hello;
import com.katruk.grpc.server.api.pb.HelloApiGrpc;
import com.katruk.grpc.server.service.HelloService;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

import static io.grpc.Status.CANCELLED;
import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelloRpc extends HelloApiGrpc.HelloApiImplBase {

    private final HelloService helloService;

    @Override
    public void trySay(Hello.HelloRequest request, StreamObserver<Hello.HelloResponse> response) {
        LocalDateTime start = now();
        try {
            Hello.HelloResponse result = this.helloService.say(request);
            success(response, start, result);
        } catch (Exception ex) {
            response.onError(Status.UNKNOWN.withDescription(ex.toString()).asRuntimeException());
            log.error("Error {}", ex.getMessage());
        }
    }

    @Override
    public void cfSay(Hello.HelloRequest request, StreamObserver<Hello.HelloResponse> response) {
        LocalDateTime start = now();
        supplyAsync(() -> this.helloService.say(request))
                .whenComplete((result, ex) -> {
                    if (ofNullable(ex).isPresent()) {
                        response.onError(ex);
                        log.error("Error {}", ex.getMessage());
                    } else {
                        success(response, start, result);
                    }
                });
    }

    private void success(StreamObserver<Hello.HelloResponse> response, LocalDateTime start, Hello.HelloResponse result) {
        if (Context.current().isCancelled()) {
            response.onError(CANCELLED
                    .withDescription("Call CANCELLED")
                    .asRuntimeException()
            );
            log.warn("Call cancelled by client! For: {}", result.getGreeting());
        } else {
            response.onNext(result);
            response.onCompleted();
            log.info("Res = {}. TimeOF: {}", result.getGreeting(), Duration.between(start, now()));
        }
    }

}