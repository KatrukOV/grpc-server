package com.katruk.grpc.server.api;

import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.function.Consumer;
import java.util.function.Function;

import static io.grpc.Status.CANCELLED;

public interface SafeRpc {

    default <T> boolean needRollback(StreamObserver<T> response, T result) {
        final boolean unSuccess;
        if (Context.current().isCancelled()) {
            response.onError(CANCELLED
                    .withDescription("Request CANCELLED by client")
                    .asRuntimeException()
            );
            unSuccess = true;
        } else {
            response.onNext(result);
            response.onCompleted();
            unSuccess = false;
        }
        return unSuccess;
    }

    default <T, R> void execute(T request, StreamObserver<R> response, Function<T, R> commit, Consumer<T> rollback) {
        try {
            R result = commit.apply(request);
            if (needRollback(response, result)) {
                rollback.accept(request);
            }
        } catch (Exception ex) {
            response.onError(Status.UNKNOWN.withDescription(ex.toString()).asRuntimeException());
            rollback.accept(request);
        }
    }

}