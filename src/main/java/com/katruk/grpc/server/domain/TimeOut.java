package com.katruk.grpc.server.domain;

import java.util.concurrent.ThreadLocalRandom;

public final class TimeOut {

    private TimeOut() {
    }

    public static void waiting(final long miliSecond) {
        try {
            Thread.sleep(miliSecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void waiting(final int min, final int max) {
        int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
        waiting(randomNum);
    }

}
