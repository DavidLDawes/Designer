package com.virtualsoundnw.designer.domain;

import static com.virtualsoundnw.designer.domain.CargoesTestSamples.*;
import static com.virtualsoundnw.designer.domain.DefensesTestSamples.*;
import static com.virtualsoundnw.designer.domain.EnginesTestSamples.*;
import static com.virtualsoundnw.designer.domain.FittingsTestSamples.*;
import static com.virtualsoundnw.designer.domain.ShipsTestSamples.*;
import static com.virtualsoundnw.designer.domain.VehiclesTestSamples.*;
import static com.virtualsoundnw.designer.domain.WeaponsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.virtualsoundnw.designer.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ShipsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ships.class);
        Ships ships1 = getShipsSample1();
        Ships ships2 = new Ships();
        assertThat(ships1).isNotEqualTo(ships2);

        ships2.setId(ships1.getId());
        assertThat(ships1).isEqualTo(ships2);

        ships2 = getShipsSample2();
        assertThat(ships1).isNotEqualTo(ships2);
    }

    @Test
    void fittingsTest() {
        Ships ships = getShipsRandomSampleGenerator();
        Fittings fittingsBack = getFittingsRandomSampleGenerator();

        ships.addFittings(fittingsBack);
        assertThat(ships.getFittings()).containsOnly(fittingsBack);
        assertThat(fittingsBack.getShips()).isEqualTo(ships);

        ships.removeFittings(fittingsBack);
        assertThat(ships.getFittings()).doesNotContain(fittingsBack);
        assertThat(fittingsBack.getShips()).isNull();

        ships.fittings(new HashSet<>(Set.of(fittingsBack)));
        assertThat(ships.getFittings()).containsOnly(fittingsBack);
        assertThat(fittingsBack.getShips()).isEqualTo(ships);

        ships.setFittings(new HashSet<>());
        assertThat(ships.getFittings()).doesNotContain(fittingsBack);
        assertThat(fittingsBack.getShips()).isNull();
    }

    @Test
    void weaponsTest() {
        Ships ships = getShipsRandomSampleGenerator();
        Weapons weaponsBack = getWeaponsRandomSampleGenerator();

        ships.addWeapons(weaponsBack);
        assertThat(ships.getWeapons()).containsOnly(weaponsBack);
        assertThat(weaponsBack.getShips()).isEqualTo(ships);

        ships.removeWeapons(weaponsBack);
        assertThat(ships.getWeapons()).doesNotContain(weaponsBack);
        assertThat(weaponsBack.getShips()).isNull();

        ships.weapons(new HashSet<>(Set.of(weaponsBack)));
        assertThat(ships.getWeapons()).containsOnly(weaponsBack);
        assertThat(weaponsBack.getShips()).isEqualTo(ships);

        ships.setWeapons(new HashSet<>());
        assertThat(ships.getWeapons()).doesNotContain(weaponsBack);
        assertThat(weaponsBack.getShips()).isNull();
    }

    @Test
    void defensesTest() {
        Ships ships = getShipsRandomSampleGenerator();
        Defenses defensesBack = getDefensesRandomSampleGenerator();

        ships.addDefenses(defensesBack);
        assertThat(ships.getDefenses()).containsOnly(defensesBack);
        assertThat(defensesBack.getShips()).isEqualTo(ships);

        ships.removeDefenses(defensesBack);
        assertThat(ships.getDefenses()).doesNotContain(defensesBack);
        assertThat(defensesBack.getShips()).isNull();

        ships.defenses(new HashSet<>(Set.of(defensesBack)));
        assertThat(ships.getDefenses()).containsOnly(defensesBack);
        assertThat(defensesBack.getShips()).isEqualTo(ships);

        ships.setDefenses(new HashSet<>());
        assertThat(ships.getDefenses()).doesNotContain(defensesBack);
        assertThat(defensesBack.getShips()).isNull();
    }

    @Test
    void cargoesTest() {
        Ships ships = getShipsRandomSampleGenerator();
        Cargoes cargoesBack = getCargoesRandomSampleGenerator();

        ships.addCargoes(cargoesBack);
        assertThat(ships.getCargoes()).containsOnly(cargoesBack);
        assertThat(cargoesBack.getShips()).isEqualTo(ships);

        ships.removeCargoes(cargoesBack);
        assertThat(ships.getCargoes()).doesNotContain(cargoesBack);
        assertThat(cargoesBack.getShips()).isNull();

        ships.cargoes(new HashSet<>(Set.of(cargoesBack)));
        assertThat(ships.getCargoes()).containsOnly(cargoesBack);
        assertThat(cargoesBack.getShips()).isEqualTo(ships);

        ships.setCargoes(new HashSet<>());
        assertThat(ships.getCargoes()).doesNotContain(cargoesBack);
        assertThat(cargoesBack.getShips()).isNull();
    }

    @Test
    void vehiclesTest() {
        Ships ships = getShipsRandomSampleGenerator();
        Vehicles vehiclesBack = getVehiclesRandomSampleGenerator();

        ships.addVehicles(vehiclesBack);
        assertThat(ships.getVehicles()).containsOnly(vehiclesBack);
        assertThat(vehiclesBack.getShips()).isEqualTo(ships);

        ships.removeVehicles(vehiclesBack);
        assertThat(ships.getVehicles()).doesNotContain(vehiclesBack);
        assertThat(vehiclesBack.getShips()).isNull();

        ships.vehicles(new HashSet<>(Set.of(vehiclesBack)));
        assertThat(ships.getVehicles()).containsOnly(vehiclesBack);
        assertThat(vehiclesBack.getShips()).isEqualTo(ships);

        ships.setVehicles(new HashSet<>());
        assertThat(ships.getVehicles()).doesNotContain(vehiclesBack);
        assertThat(vehiclesBack.getShips()).isNull();
    }

    @Test
    void enginesTest() {
        Ships ships = getShipsRandomSampleGenerator();
        Engines enginesBack = getEnginesRandomSampleGenerator();

        ships.addEngines(enginesBack);
        assertThat(ships.getEngines()).containsOnly(enginesBack);
        assertThat(enginesBack.getShips()).isEqualTo(ships);

        ships.removeEngines(enginesBack);
        assertThat(ships.getEngines()).doesNotContain(enginesBack);
        assertThat(enginesBack.getShips()).isNull();

        ships.engines(new HashSet<>(Set.of(enginesBack)));
        assertThat(ships.getEngines()).containsOnly(enginesBack);
        assertThat(enginesBack.getShips()).isEqualTo(ships);

        ships.setEngines(new HashSet<>());
        assertThat(ships.getEngines()).doesNotContain(enginesBack);
        assertThat(enginesBack.getShips()).isNull();
    }
}
