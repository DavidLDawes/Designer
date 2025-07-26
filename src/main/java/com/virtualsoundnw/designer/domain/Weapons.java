package com.virtualsoundnw.designer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.virtualsoundnw.designer.domain.enumeration.Weapon;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Weapons.
 */
@Entity
@Table(name = "weapons")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Weapons implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "weapon", nullable = false)
    private Weapon weapon;

    @NotNull
    @Column(name = "w_ship_id", nullable = false)
    private Integer wShipId;

    @NotNull
    @Column(name = "battery", nullable = false)
    private Integer battery;

    @NotNull
    @Column(name = "count", nullable = false)
    private Integer count;

    @NotNull
    @Column(name = "mass", nullable = false)
    private Float mass;

    @NotNull
    @Column(name = "cost", nullable = false)
    private Float cost;

    @NotNull
    @Column(name = "armored", nullable = false)
    private Boolean armored;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "fittings", "weapons", "defenses", "cargoes", "vehicles", "engines" }, allowSetters = true)
    private Ships ships;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Weapons id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Weapon getWeapon() {
        return this.weapon;
    }

    public Weapons weapon(Weapon weapon) {
        this.setWeapon(weapon);
        return this;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Integer getwShipId() {
        return this.wShipId;
    }

    public Weapons wShipId(Integer wShipId) {
        this.setwShipId(wShipId);
        return this;
    }

    public void setwShipId(Integer wShipId) {
        this.wShipId = wShipId;
    }

    public Integer getBattery() {
        return this.battery;
    }

    public Weapons battery(Integer battery) {
        this.setBattery(battery);
        return this;
    }

    public void setBattery(Integer battery) {
        this.battery = battery;
    }

    public Integer getCount() {
        return this.count;
    }

    public Weapons count(Integer count) {
        this.setCount(count);
        return this;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Float getMass() {
        return this.mass;
    }

    public Weapons mass(Float mass) {
        this.setMass(mass);
        return this;
    }

    public void setMass(Float mass) {
        this.mass = mass;
    }

    public Float getCost() {
        return this.cost;
    }

    public Weapons cost(Float cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Boolean getArmored() {
        return this.armored;
    }

    public Weapons armored(Boolean armored) {
        this.setArmored(armored);
        return this;
    }

    public void setArmored(Boolean armored) {
        this.armored = armored;
    }

    public Ships getShips() {
        return this.ships;
    }

    public void setShips(Ships ships) {
        this.ships = ships;
    }

    public Weapons ships(Ships ships) {
        this.setShips(ships);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Weapons)) {
            return false;
        }
        return getId() != null && getId().equals(((Weapons) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Weapons{" +
            "id=" + getId() +
            ", weapon='" + getWeapon() + "'" +
            ", wShipId=" + getwShipId() +
            ", battery=" + getBattery() +
            ", count=" + getCount() +
            ", mass=" + getMass() +
            ", cost=" + getCost() +
            ", armored='" + getArmored() + "'" +
            "}";
    }
}
