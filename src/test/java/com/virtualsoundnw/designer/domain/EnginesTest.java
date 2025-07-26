package com.virtualsoundnw.designer.domain;

import static com.virtualsoundnw.designer.domain.EnginesTestSamples.*;
import static com.virtualsoundnw.designer.domain.ShipsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.virtualsoundnw.designer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EnginesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Engines.class);
        Engines engines1 = getEnginesSample1();
        Engines engines2 = new Engines();
        assertThat(engines1).isNotEqualTo(engines2);

        engines2.setId(engines1.getId());
        assertThat(engines1).isEqualTo(engines2);

        engines2 = getEnginesSample2();
        assertThat(engines1).isNotEqualTo(engines2);
    }

    @Test
    void shipsTest() {
        Engines engines = getEnginesRandomSampleGenerator();
        Ships shipsBack = getShipsRandomSampleGenerator();

        engines.setShips(shipsBack);
        assertThat(engines.getShips()).isEqualTo(shipsBack);

        engines.ships(null);
        assertThat(engines.getShips()).isNull();
    }
}
