package com.katruk.grpc.server.domain;

public final class TimeOut {

    private final long milliSecond;

    public TimeOut(long mSecond) {
        this.milliSecond = mSecond;
    }

    public void waiting() {
        try {
            Thread.sleep(milliSecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
