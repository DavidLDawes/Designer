package com.virtualsoundnw.designer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.virtualsoundnw.designer.domain.enumeration.Vehicle;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vehicles.
 */
@Entity
@Table(name = "vehicles")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vehicles implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle", nullable = false)
    private Vehicle vehicle;

    @NotNull
    @Column(name = "v_ship_id", nullable = false)
    private Integer vShipId;

    @NotNull
    @Column(name = "mass", nullable = false)
    private Float mass;

    @NotNull
    @Column(name = "cost", nullable = false)
    private Float cost;

    @NotNull
    @Column(name = "armored", nullable = false)
    private Boolean armored;

    @NotNull
    @Column(name = "repair_bay", nullable = false)
    private Boolean repairBay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "fittings", "weapons", "defenses", "cargoes", "vehicles", "engines" }, allowSetters = true)
    private Ships ships;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vehicles id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public Vehicles vehicle(Vehicle vehicle) {
        this.setVehicle(vehicle);
        return this;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Integer getvShipId() {
        return this.vShipId;
    }

    public Vehicles vShipId(Integer vShipId) {
        this.setvShipId(vShipId);
        return this;
    }

    public void setvShipId(Integer vShipId) {
        this.vShipId = vShipId;
    }

    public Float getMass() {
        return this.mass;
    }

    public Vehicles mass(Float mass) {
        this.setMass(mass);
        return this;
    }

    public void setMass(Float mass) {
        this.mass = mass;
    }

    public Float getCost() {
        return this.cost;
    }

    public Vehicles cost(Float cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Boolean getArmored() {
        return this.armored;
    }

    public Vehicles armored(Boolean armored) {
        this.setArmored(armored);
        return this;
    }

    public void setArmored(Boolean armored) {
        this.armored = armored;
    }

    public Boolean getRepairBay() {
        return this.repairBay;
    }

    public Vehicles repairBay(Boolean repairBay) {
        this.setRepairBay(repairBay);
        return this;
    }

    public void setRepairBay(Boolean repairBay) {
        this.repairBay = repairBay;
    }

    public Ships getShips() {
        return this.ships;
    }

    public void setShips(Ships ships) {
        this.ships = ships;
    }

    public Vehicles ships(Ships ships) {
        this.setShips(ships);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vehicles)) {
            return false;
        }
        return getId() != null && getId().equals(((Vehicles) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vehicles{" +
            "id=" + getId() +
            ", vehicle='" + getVehicle() + "'" +
            ", vShipId=" + getvShipId() +
            ", mass=" + getMass() +
            ", cost=" + getCost() +
            ", armored='" + getArmored() + "'" +
            ", repairBay='" + getRepairBay() + "'" +
            "}";
    }
}
