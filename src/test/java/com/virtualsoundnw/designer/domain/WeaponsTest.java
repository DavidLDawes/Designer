package com.virtualsoundnw.designer.domain;

import static com.virtualsoundnw.designer.domain.ShipsTestSamples.*;
import static com.virtualsoundnw.designer.domain.WeaponsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.virtualsoundnw.designer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WeaponsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Weapons.class);
        Weapons weapons1 = getWeaponsSample1();
        Weapons weapons2 = new Weapons();
        assertThat(weapons1).isNotEqualTo(weapons2);

        weapons2.setId(weapons1.getId());
        assertThat(weapons1).isEqualTo(weapons2);

        weapons2 = getWeaponsSample2();
        assertThat(weapons1).isNotEqualTo(weapons2);
    }

    @Test
    void shipsTest() {
        Weapons weapons = getWeaponsRandomSampleGenerator();
        Ships shipsBack = getShipsRandomSampleGenerator();

        weapons.setShips(shipsBack);
        assertThat(weapons.getShips()).isEqualTo(shipsBack);

        weapons.ships(null);
        assertThat(weapons.getShips()).isNull();
    }
}
