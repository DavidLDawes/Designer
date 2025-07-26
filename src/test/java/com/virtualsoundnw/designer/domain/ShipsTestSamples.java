package com.virtualsoundnw.designer.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ShipsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Ships getShipsSample1() {
        return new Ships().id(1L).shipId(1).userId(1).tonnage(1).code("code1").hull(1).structure(1).sections(1);
    }

    public static Ships getShipsSample2() {
        return new Ships().id(2L).shipId(2).userId(2).tonnage(2).code("code2").hull(2).structure(2).sections(2);
    }

    public static Ships getShipsRandomSampleGenerator() {
        return new Ships()
            .id(longCount.incrementAndGet())
            .shipId(intCount.incrementAndGet())
            .userId(intCount.incrementAndGet())
            .tonnage(intCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .hull(intCount.incrementAndGet())
            .structure(intCount.incrementAndGet())
            .sections(intCount.incrementAndGet());
    }
}
