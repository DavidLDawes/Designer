package com.virtualsoundnw.designer.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CargoesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Cargoes getCargoesSample1() {
        return new Cargoes().id(1L).cShipId(1);
    }

    public static Cargoes getCargoesSample2() {
        return new Cargoes().id(2L).cShipId(2);
    }

    public static Cargoes getCargoesRandomSampleGenerator() {
        return new Cargoes().id(longCount.incrementAndGet()).cShipId(intCount.incrementAndGet());
    }
}
