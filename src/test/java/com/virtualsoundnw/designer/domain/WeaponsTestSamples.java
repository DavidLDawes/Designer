package com.virtualsoundnw.designer.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class WeaponsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Weapons getWeaponsSample1() {
        return new Weapons().id(1L).wShipId(1).battery(1).count(1);
    }

    public static Weapons getWeaponsSample2() {
        return new Weapons().id(2L).wShipId(2).battery(2).count(2);
    }

    public static Weapons getWeaponsRandomSampleGenerator() {
        return new Weapons()
            .id(longCount.incrementAndGet())
            .wShipId(intCount.incrementAndGet())
            .battery(intCount.incrementAndGet())
            .count(intCount.incrementAndGet());
    }
}
