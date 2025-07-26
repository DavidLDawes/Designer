package com.virtualsoundnw.designer.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FittingsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Fittings getFittingsSample1() {
        return new Fittings().id(1L).fShipId(1).count(1);
    }

    public static Fittings getFittingsSample2() {
        return new Fittings().id(2L).fShipId(2).count(2);
    }

    public static Fittings getFittingsRandomSampleGenerator() {
        return new Fittings().id(longCount.incrementAndGet()).fShipId(intCount.incrementAndGet()).count(intCount.incrementAndGet());
    }
}
