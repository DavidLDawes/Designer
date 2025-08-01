package com.virtualsoundnw.designer.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class WeaponsAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWeaponsAllPropertiesEquals(Weapons expected, Weapons actual) {
        assertWeaponsAutoGeneratedPropertiesEquals(expected, actual);
        assertWeaponsAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWeaponsAllUpdatablePropertiesEquals(Weapons expected, Weapons actual) {
        assertWeaponsUpdatableFieldsEquals(expected, actual);
        assertWeaponsUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWeaponsAutoGeneratedPropertiesEquals(Weapons expected, Weapons actual) {
        assertThat(actual)
            .as("Verify Weapons auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWeaponsUpdatableFieldsEquals(Weapons expected, Weapons actual) {
        assertThat(actual)
            .as("Verify Weapons relevant properties")
            .satisfies(a -> assertThat(a.getWeapon()).as("check weapon").isEqualTo(expected.getWeapon()))
            .satisfies(a -> assertThat(a.getwShipId()).as("check wShipId").isEqualTo(expected.getwShipId()))
            .satisfies(a -> assertThat(a.getBattery()).as("check battery").isEqualTo(expected.getBattery()))
            .satisfies(a -> assertThat(a.getCount()).as("check count").isEqualTo(expected.getCount()))
            .satisfies(a -> assertThat(a.getMass()).as("check mass").isEqualTo(expected.getMass()))
            .satisfies(a -> assertThat(a.getCost()).as("check cost").isEqualTo(expected.getCost()))
            .satisfies(a -> assertThat(a.getArmored()).as("check armored").isEqualTo(expected.getArmored()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWeaponsUpdatableRelationshipsEquals(Weapons expected, Weapons actual) {
        assertThat(actual)
            .as("Verify Weapons relationships")
            .satisfies(a -> assertThat(a.getShips()).as("check ships").isEqualTo(expected.getShips()));
    }
}
