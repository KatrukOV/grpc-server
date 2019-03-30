package com.katruk.grpc.server.domain;

import java.util.concurrent.ThreadLocalRandom;

public final class Random {

    private final int min;
    private final int max;

    public Random(int minValue, int maxValue) {
        this.min = minValue;
        this.max = maxValue;
    }

    public int intInRange() {
        return ThreadLocalRandom.current()
                .nextInt(this.min, this.max + 1);
    }

}
