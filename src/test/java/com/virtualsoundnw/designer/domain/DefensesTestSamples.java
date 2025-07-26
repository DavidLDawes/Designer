package com.virtualsoundnw.designer.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DefensesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Defenses getDefensesSample1() {
        return new Defenses().id(1L).dShipId(1).battery(1).count(1);
    }

    public static Defenses getDefensesSample2() {
        return new Defenses().id(2L).dShipId(2).battery(2).count(2);
    }

    public static Defenses getDefensesRandomSampleGenerator() {
        return new Defenses()
            .id(longCount.incrementAndGet())
            .dShipId(intCount.incrementAndGet())
            .battery(intCount.incrementAndGet())
            .count(intCount.incrementAndGet());
    }
}
