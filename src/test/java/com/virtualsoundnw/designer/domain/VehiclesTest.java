package com.virtualsoundnw.designer.domain;

import static com.virtualsoundnw.designer.domain.ShipsTestSamples.*;
import static com.virtualsoundnw.designer.domain.VehiclesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.virtualsoundnw.designer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehiclesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vehicles.class);
        Vehicles vehicles1 = getVehiclesSample1();
        Vehicles vehicles2 = new Vehicles();
        assertThat(vehicles1).isNotEqualTo(vehicles2);

        vehicles2.setId(vehicles1.getId());
        assertThat(vehicles1).isEqualTo(vehicles2);

        vehicles2 = getVehiclesSample2();
        assertThat(vehicles1).isNotEqualTo(vehicles2);
    }

    @Test
    void shipsTest() {
        Vehicles vehicles = getVehiclesRandomSampleGenerator();
        Ships shipsBack = getShipsRandomSampleGenerator();

        vehicles.setShips(shipsBack);
        assertThat(vehicles.getShips()).isEqualTo(shipsBack);

        vehicles.ships(null);
        assertThat(vehicles.getShips()).isNull();
    }
}
