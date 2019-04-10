package com.katruk.grpc.server.api;

import com.katruk.grpc.server.api.pb.Hello;
import com.katruk.grpc.server.api.pb.HelloApiGrpc;
import com.katruk.grpc.server.service.HelloService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelloRpc extends HelloApiGrpc.HelloApiImplBase implements TransactionalRpc {

    private final HelloService helloService;

    @Override
    public void trySay(Hello.HelloRequest request, StreamObserver<Hello.HelloResponse> response) {
        Function<Hello.HelloRequest, Hello.HelloResponse> commit = e -> {
            Hello.HelloResponse result = this.helloService.say(request);
            log.info("Ok to {}", e.getName());
            return result;
        };
        Consumer<Hello.HelloRequest> rollback = e -> {
            log.warn("Rollback for {}", e.getName());
            this.helloService.revertSay(request);
        };
        execute(request, response, commit, rollback);
    }

    @Override
    public void cfSay(Hello.HelloRequest request, StreamObserver<Hello.HelloResponse> response) {
        supplyAsync(() -> this.helloService.say(request))
                .whenComplete((result, ex) -> {
                    if (ofNullable(ex).isPresent()) {
                        response.onError(ex);
                        log.error("Error {}", ex.getMessage());
                    } else {
                        if (needRollback(response, this.helloService.say(request))) {
                            this.helloService.revertSay(request);
                        }
                    }
                });
    }

}