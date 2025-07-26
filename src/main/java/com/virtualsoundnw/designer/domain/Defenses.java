package com.virtualsoundnw.designer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.virtualsoundnw.designer.domain.enumeration.Defense;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Defenses.
 */
@Entity
@Table(name = "defenses")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Defenses implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "defense", nullable = false)
    private Defense defense;

    @NotNull
    @Column(name = "d_ship_id", nullable = false)
    private Integer dShipId;

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

    public Defenses id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Defense getDefense() {
        return this.defense;
    }

    public Defenses defense(Defense defense) {
        this.setDefense(defense);
        return this;
    }

    public void setDefense(Defense defense) {
        this.defense = defense;
    }

    public Integer getdShipId() {
        return this.dShipId;
    }

    public Defenses dShipId(Integer dShipId) {
        this.setdShipId(dShipId);
        return this;
    }

    public void setdShipId(Integer dShipId) {
        this.dShipId = dShipId;
    }

    public Integer getBattery() {
        return this.battery;
    }

    public Defenses battery(Integer battery) {
        this.setBattery(battery);
        return this;
    }

    public void setBattery(Integer battery) {
        this.battery = battery;
    }

    public Integer getCount() {
        return this.count;
    }

    public Defenses count(Integer count) {
        this.setCount(count);
        return this;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Float getMass() {
        return this.mass;
    }

    public Defenses mass(Float mass) {
        this.setMass(mass);
        return this;
    }

    public void setMass(Float mass) {
        this.mass = mass;
    }

    public Float getCost() {
        return this.cost;
    }

    public Defenses cost(Float cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Boolean getArmored() {
        return this.armored;
    }

    public Defenses armored(Boolean armored) {
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

    public Defenses ships(Ships ships) {
        this.setShips(ships);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Defenses)) {
            return false;
        }
        return getId() != null && getId().equals(((Defenses) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Defenses{" +
            "id=" + getId() +
            ", defense='" + getDefense() + "'" +
            ", dShipId=" + getdShipId() +
            ", battery=" + getBattery() +
            ", count=" + getCount() +
            ", mass=" + getMass() +
            ", cost=" + getCost() +
            ", armored='" + getArmored() + "'" +
            "}";
    }
}
