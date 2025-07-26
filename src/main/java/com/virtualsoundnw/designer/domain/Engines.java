package com.virtualsoundnw.designer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.virtualsoundnw.designer.domain.enumeration.Engine;
import com.virtualsoundnw.designer.domain.enumeration.TL;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Engines.
 */
@Entity
@Table(name = "engines")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Engines implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "e_ship_id", nullable = false)
    private Integer eShipId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "engine", nullable = false)
    private Engine engine;

    @NotNull
    @Column(name = "mass", nullable = false)
    private Float mass;

    @NotNull
    @Column(name = "cost", nullable = false)
    private Float cost;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tl", nullable = false)
    private TL tl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "fittings", "weapons", "defenses", "cargoes", "vehicles", "engines" }, allowSetters = true)
    private Ships ships;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Engines id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer geteShipId() {
        return this.eShipId;
    }

    public Engines eShipId(Integer eShipId) {
        this.seteShipId(eShipId);
        return this;
    }

    public void seteShipId(Integer eShipId) {
        this.eShipId = eShipId;
    }

    public Engine getEngine() {
        return this.engine;
    }

    public Engines engine(Engine engine) {
        this.setEngine(engine);
        return this;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public Float getMass() {
        return this.mass;
    }

    public Engines mass(Float mass) {
        this.setMass(mass);
        return this;
    }

    public void setMass(Float mass) {
        this.mass = mass;
    }

    public Float getCost() {
        return this.cost;
    }

    public Engines cost(Float cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public TL getTl() {
        return this.tl;
    }

    public Engines tl(TL tl) {
        this.setTl(tl);
        return this;
    }

    public void setTl(TL tl) {
        this.tl = tl;
    }

    public Ships getShips() {
        return this.ships;
    }

    public void setShips(Ships ships) {
        this.ships = ships;
    }

    public Engines ships(Ships ships) {
        this.setShips(ships);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Engines)) {
            return false;
        }
        return getId() != null && getId().equals(((Engines) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Engines{" +
            "id=" + getId() +
            ", eShipId=" + geteShipId() +
            ", engine='" + getEngine() + "'" +
            ", mass=" + getMass() +
            ", cost=" + getCost() +
            ", tl='" + getTl() + "'" +
            "}";
    }
}
