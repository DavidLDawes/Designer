package com.virtualsoundnw.designer.domain;

import static com.virtualsoundnw.designer.domain.FittingsTestSamples.*;
import static com.virtualsoundnw.designer.domain.ShipsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.virtualsoundnw.designer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FittingsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fittings.class);
        Fittings fittings1 = getFittingsSample1();
        Fittings fittings2 = new Fittings();
        assertThat(fittings1).isNotEqualTo(fittings2);

        fittings2.setId(fittings1.getId());
        assertThat(fittings1).isEqualTo(fittings2);

        fittings2 = getFittingsSample2();
        assertThat(fittings1).isNotEqualTo(fittings2);
    }

    @Test
    void shipsTest() {
        Fittings fittings = getFittingsRandomSampleGenerator();
        Ships shipsBack = getShipsRandomSampleGenerator();

        fittings.setShips(shipsBack);
        assertThat(fittings.getShips()).isEqualTo(shipsBack);

        fittings.ships(null);
        assertThat(fittings.getShips()).isNull();
    }
}
