package com.katruk.grpc.server.api;

import com.katruk.grpc.server.api.pb.Hello;
import com.katruk.grpc.server.api.pb.HelloApiGrpc;
import com.katruk.grpc.server.service.HelloService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelloRpc extends HelloApiGrpc.HelloApiImplBase implements SafeRpc {

    private final HelloService helloService;
    public static List<String> storeResult = new CopyOnWriteArrayList<>();

    @Override
    public void trySay(Hello.HelloRequest request, StreamObserver<Hello.HelloResponse> response) {
        Function<Hello.HelloRequest, Hello.HelloResponse> commit = e -> {
            Hello.HelloResponse say = this.helloService.say(request);
            if(storeResult.contains(request.getName())){
                log.error("Duplicate: {}",request.getName());
            }
            log.info("++ {}",request.getName());
            storeResult.add(request.getName());
            return say;
        };
        Consumer<Hello.HelloRequest> rollback = e -> this.helloService.revertSay(request);
        execute(request, response, commit, rollback);
        log.info("! size {}, all {}", storeResult.size(), storeResult);
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