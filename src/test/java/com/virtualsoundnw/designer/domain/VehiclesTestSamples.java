package com.virtualsoundnw.designer.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VehiclesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Vehicles getVehiclesSample1() {
        return new Vehicles().id(1L).vShipId(1);
    }

    public static Vehicles getVehiclesSample2() {
        return new Vehicles().id(2L).vShipId(2);
    }

    public static Vehicles getVehiclesRandomSampleGenerator() {
        return new Vehicles().id(longCount.incrementAndGet()).vShipId(intCount.incrementAndGet());
    }
}
