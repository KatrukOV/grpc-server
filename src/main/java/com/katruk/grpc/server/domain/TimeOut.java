package com.katruk.grpc.server.domain;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class TimeOut {

    private final long milliSecond;

    public TimeOut(long mSecond) {
        this.milliSecond = mSecond;
    }

    public void waiting() {
        try {
            Thread.sleep(milliSecond);
        } catch (InterruptedException e) {
            log.warn("waiting failed: {}", e.getMessage());
        }
    }

}
