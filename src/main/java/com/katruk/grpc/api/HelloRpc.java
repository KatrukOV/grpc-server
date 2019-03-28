package com.katruk.grpc.api;

import com.katruk.grpc.api.pb.Hello;
import com.katruk.grpc.api.pb.HelloApiGrpc;
import com.katruk.grpc.service.HelloService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelloRpc extends HelloApiGrpc.HelloApiImplBase {

    private final HelloService helloService;

    @Override
    public void say(Hello.HelloRequest request, StreamObserver<Hello.HelloResponse> response) {
        try {
            Hello.HelloResponse result = this.helloService.say(request);
            response.onNext(result);
            response.onCompleted();
            log.info("+++");
        } catch (Exception ex) {
            response.onError(ex);
            log.error("---");
        }
    }

}