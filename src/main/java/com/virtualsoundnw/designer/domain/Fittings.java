package com.virtualsoundnw.designer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.virtualsoundnw.designer.domain.enumeration.Fitting;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Fittings.
 */
@Entity
@Table(name = "fittings")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Fittings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "fitting", nullable = false)
    private Fitting fitting;

    @NotNull
    @Column(name = "f_ship_id", nullable = false)
    private Integer fShipId;

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

    public Fittings id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fitting getFitting() {
        return this.fitting;
    }

    public Fittings fitting(Fitting fitting) {
        this.setFitting(fitting);
        return this;
    }

    public void setFitting(Fitting fitting) {
        this.fitting = fitting;
    }

    public Integer getfShipId() {
        return this.fShipId;
    }

    public Fittings fShipId(Integer fShipId) {
        this.setfShipId(fShipId);
        return this;
    }

    public void setfShipId(Integer fShipId) {
        this.fShipId = fShipId;
    }

    public Integer getCount() {
        return this.count;
    }

    public Fittings count(Integer count) {
        this.setCount(count);
        return this;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Float getMass() {
        return this.mass;
    }

    public Fittings mass(Float mass) {
        this.setMass(mass);
        return this;
    }

    public void setMass(Float mass) {
        this.mass = mass;
    }

    public Float getCost() {
        return this.cost;
    }

    public Fittings cost(Float cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Boolean getArmored() {
        return this.armored;
    }

    public Fittings armored(Boolean armored) {
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

    public Fittings ships(Ships ships) {
        this.setShips(ships);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fittings)) {
            return false;
        }
        return getId() != null && getId().equals(((Fittings) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fittings{" +
            "id=" + getId() +
            ", fitting='" + getFitting() + "'" +
            ", fShipId=" + getfShipId() +
            ", count=" + getCount() +
            ", mass=" + getMass() +
            ", cost=" + getCost() +
            ", armored='" + getArmored() + "'" +
            "}";
    }
}
