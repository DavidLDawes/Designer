package com.virtualsoundnw.designer.domain;

import static com.virtualsoundnw.designer.domain.CargoesTestSamples.*;
import static com.virtualsoundnw.designer.domain.ShipsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.virtualsoundnw.designer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CargoesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cargoes.class);
        Cargoes cargoes1 = getCargoesSample1();
        Cargoes cargoes2 = new Cargoes();
        assertThat(cargoes1).isNotEqualTo(cargoes2);

        cargoes2.setId(cargoes1.getId());
        assertThat(cargoes1).isEqualTo(cargoes2);

        cargoes2 = getCargoesSample2();
        assertThat(cargoes1).isNotEqualTo(cargoes2);
    }

    @Test
    void shipsTest() {
        Cargoes cargoes = getCargoesRandomSampleGenerator();
        Ships shipsBack = getShipsRandomSampleGenerator();

        cargoes.setShips(shipsBack);
        assertThat(cargoes.getShips()).isEqualTo(shipsBack);

        cargoes.ships(null);
        assertThat(cargoes.getShips()).isNull();
    }
}
