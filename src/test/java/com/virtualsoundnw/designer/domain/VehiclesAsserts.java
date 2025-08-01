package com.virtualsoundnw.designer.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class VehiclesAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVehiclesAllPropertiesEquals(Vehicles expected, Vehicles actual) {
        assertVehiclesAutoGeneratedPropertiesEquals(expected, actual);
        assertVehiclesAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVehiclesAllUpdatablePropertiesEquals(Vehicles expected, Vehicles actual) {
        assertVehiclesUpdatableFieldsEquals(expected, actual);
        assertVehiclesUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVehiclesAutoGeneratedPropertiesEquals(Vehicles expected, Vehicles actual) {
        assertThat(actual)
            .as("Verify Vehicles auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVehiclesUpdatableFieldsEquals(Vehicles expected, Vehicles actual) {
        assertThat(actual)
            .as("Verify Vehicles relevant properties")
            .satisfies(a -> assertThat(a.getVehicle()).as("check vehicle").isEqualTo(expected.getVehicle()))
            .satisfies(a -> assertThat(a.getvShipId()).as("check vShipId").isEqualTo(expected.getvShipId()))
            .satisfies(a -> assertThat(a.getMass()).as("check mass").isEqualTo(expected.getMass()))
            .satisfies(a -> assertThat(a.getCost()).as("check cost").isEqualTo(expected.getCost()))
            .satisfies(a -> assertThat(a.getArmored()).as("check armored").isEqualTo(expected.getArmored()))
            .satisfies(a -> assertThat(a.getRepairBay()).as("check repairBay").isEqualTo(expected.getRepairBay()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVehiclesUpdatableRelationshipsEquals(Vehicles expected, Vehicles actual) {
        assertThat(actual)
            .as("Verify Vehicles relationships")
            .satisfies(a -> assertThat(a.getShips()).as("check ships").isEqualTo(expected.getShips()));
    }
}
