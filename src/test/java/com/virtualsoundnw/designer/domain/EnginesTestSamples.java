package com.virtualsoundnw.designer.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EnginesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Engines getEnginesSample1() {
        return new Engines().id(1L).eShipId(1);
    }

    public static Engines getEnginesSample2() {
        return new Engines().id(2L).eShipId(2);
    }

    public static Engines getEnginesRandomSampleGenerator() {
        return new Engines().id(longCount.incrementAndGet()).eShipId(intCount.incrementAndGet());
    }
}
