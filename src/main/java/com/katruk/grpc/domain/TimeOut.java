package com.katruk.grpc.domain;

public final class TimeOut {

    private TimeOut() {
    }

    public static void waiting(final long second) {
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
