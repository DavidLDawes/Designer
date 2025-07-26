package com.virtualsoundnw.designer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.virtualsoundnw.designer.domain.enumeration.Cargo;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cargoes.
 */
@Entity
@Table(name = "cargoes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cargoes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "cargo", nullable = false)
    private Cargo cargo;

    @NotNull
    @Column(name = "c_ship_id", nullable = false)
    private Integer cShipId;

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

    public Cargoes id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cargo getCargo() {
        return this.cargo;
    }

    public Cargoes cargo(Cargo cargo) {
        this.setCargo(cargo);
        return this;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Integer getcShipId() {
        return this.cShipId;
    }

    public Cargoes cShipId(Integer cShipId) {
        this.setcShipId(cShipId);
        return this;
    }

    public void setcShipId(Integer cShipId) {
        this.cShipId = cShipId;
    }

    public Float getMass() {
        return this.mass;
    }

    public Cargoes mass(Float mass) {
        this.setMass(mass);
        return this;
    }

    public void setMass(Float mass) {
        this.mass = mass;
    }

    public Float getCost() {
        return this.cost;
    }

    public Cargoes cost(Float cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Boolean getArmored() {
        return this.armored;
    }

    public Cargoes armored(Boolean armored) {
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

    public Cargoes ships(Ships ships) {
        this.setShips(ships);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cargoes)) {
            return false;
        }
        return getId() != null && getId().equals(((Cargoes) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cargoes{" +
            "id=" + getId() +
            ", cargo='" + getCargo() + "'" +
            ", cShipId=" + getcShipId() +
            ", mass=" + getMass() +
            ", cost=" + getCost() +
            ", armored='" + getArmored() + "'" +
            "}";
    }
}
