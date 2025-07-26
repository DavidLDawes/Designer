package com.virtualsoundnw.designer.domain;

import static com.virtualsoundnw.designer.domain.DefensesTestSamples.*;
import static com.virtualsoundnw.designer.domain.ShipsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.virtualsoundnw.designer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DefensesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Defenses.class);
        Defenses defenses1 = getDefensesSample1();
        Defenses defenses2 = new Defenses();
        assertThat(defenses1).isNotEqualTo(defenses2);

        defenses2.setId(defenses1.getId());
        assertThat(defenses1).isEqualTo(defenses2);

        defenses2 = getDefensesSample2();
        assertThat(defenses1).isNotEqualTo(defenses2);
    }

    @Test
    void shipsTest() {
        Defenses defenses = getDefensesRandomSampleGenerator();
        Ships shipsBack = getShipsRandomSampleGenerator();

        defenses.setShips(shipsBack);
        assertThat(defenses.getShips()).isEqualTo(shipsBack);

        defenses.ships(null);
        assertThat(defenses.getShips()).isNull();
    }
}
