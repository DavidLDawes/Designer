package com.virtualsoundnw.designer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.virtualsoundnw.designer.domain.enumeration.Config;
import com.virtualsoundnw.designer.domain.enumeration.TL;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ships.
 */
@Entity
@Table(name = "ships")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ships implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "ship_id", nullable = false)
    private Integer shipId;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tl", nullable = false)
    private TL tl;

    @NotNull
    @Column(name = "tonnage", nullable = false)
    private Integer tonnage;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "conf", nullable = false)
    private Config conf;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "hull", nullable = false)
    private Integer hull;

    @NotNull
    @Column(name = "structure", nullable = false)
    private Integer structure;

    @NotNull
    @Column(name = "sections", nullable = false)
    private Integer sections;

    @NotNull
    @Column(name = "capital", nullable = false)
    private Boolean capital;

    @NotNull
    @Column(name = "military", nullable = false)
    private Boolean military;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ships")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ships" }, allowSetters = true)
    private Set<Fittings> fittings = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ships")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ships" }, allowSetters = true)
    private Set<Weapons> weapons = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ships")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ships" }, allowSetters = true)
    private Set<Defenses> defenses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ships")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ships" }, allowSetters = true)
    private Set<Cargoes> cargoes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ships")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ships" }, allowSetters = true)
    private Set<Vehicles> vehicles = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ships")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ships" }, allowSetters = true)
    private Set<Engines> engines = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ships id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getShipId() {
        return this.shipId;
    }

    public Ships shipId(Integer shipId) {
        this.setShipId(shipId);
        return this;
    }

    public void setShipId(Integer shipId) {
        this.shipId = shipId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public Ships userId(Integer userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public TL getTl() {
        return this.tl;
    }

    public Ships tl(TL tl) {
        this.setTl(tl);
        return this;
    }

    public void setTl(TL tl) {
        this.tl = tl;
    }

    public Integer getTonnage() {
        return this.tonnage;
    }

    public Ships tonnage(Integer tonnage) {
        this.setTonnage(tonnage);
        return this;
    }

    public void setTonnage(Integer tonnage) {
        this.tonnage = tonnage;
    }

    public Config getConf() {
        return this.conf;
    }

    public Ships conf(Config conf) {
        this.setConf(conf);
        return this;
    }

    public void setConf(Config conf) {
        this.conf = conf;
    }

    public String getCode() {
        return this.code;
    }

    public Ships code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getHull() {
        return this.hull;
    }

    public Ships hull(Integer hull) {
        this.setHull(hull);
        return this;
    }

    public void setHull(Integer hull) {
        this.hull = hull;
    }

    public Integer getStructure() {
        return this.structure;
    }

    public Ships structure(Integer structure) {
        this.setStructure(structure);
        return this;
    }

    public void setStructure(Integer structure) {
        this.structure = structure;
    }

    public Integer getSections() {
        return this.sections;
    }

    public Ships sections(Integer sections) {
        this.setSections(sections);
        return this;
    }

    public void setSections(Integer sections) {
        this.sections = sections;
    }

    public Boolean getCapital() {
        return this.capital;
    }

    public Ships capital(Boolean capital) {
        this.setCapital(capital);
        return this;
    }

    public void setCapital(Boolean capital) {
        this.capital = capital;
    }

    public Boolean getMilitary() {
        return this.military;
    }

    public Ships military(Boolean military) {
        this.setMilitary(military);
        return this;
    }

    public void setMilitary(Boolean military) {
        this.military = military;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ships user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Fittings> getFittings() {
        return this.fittings;
    }

    public void setFittings(Set<Fittings> fittings) {
        if (this.fittings != null) {
            this.fittings.forEach(i -> i.setShips(null));
        }
        if (fittings != null) {
            fittings.forEach(i -> i.setShips(this));
        }
        this.fittings = fittings;
    }

    public Ships fittings(Set<Fittings> fittings) {
        this.setFittings(fittings);
        return this;
    }

    public Ships addFittings(Fittings fittings) {
        this.fittings.add(fittings);
        fittings.setShips(this);
        return this;
    }

    public Ships removeFittings(Fittings fittings) {
        this.fittings.remove(fittings);
        fittings.setShips(null);
        return this;
    }

    public Set<Weapons> getWeapons() {
        return this.weapons;
    }

    public void setWeapons(Set<Weapons> weapons) {
        if (this.weapons != null) {
            this.weapons.forEach(i -> i.setShips(null));
        }
        if (weapons != null) {
            weapons.forEach(i -> i.setShips(this));
        }
        this.weapons = weapons;
    }

    public Ships weapons(Set<Weapons> weapons) {
        this.setWeapons(weapons);
        return this;
    }

    public Ships addWeapons(Weapons weapons) {
        this.weapons.add(weapons);
        weapons.setShips(this);
        return this;
    }

    public Ships removeWeapons(Weapons weapons) {
        this.weapons.remove(weapons);
        weapons.setShips(null);
        return this;
    }

    public Set<Defenses> getDefenses() {
        return this.defenses;
    }

    public void setDefenses(Set<Defenses> defenses) {
        if (this.defenses != null) {
            this.defenses.forEach(i -> i.setShips(null));
        }
        if (defenses != null) {
            defenses.forEach(i -> i.setShips(this));
        }
        this.defenses = defenses;
    }

    public Ships defenses(Set<Defenses> defenses) {
        this.setDefenses(defenses);
        return this;
    }

    public Ships addDefenses(Defenses defenses) {
        this.defenses.add(defenses);
        defenses.setShips(this);
        return this;
    }

    public Ships removeDefenses(Defenses defenses) {
        this.defenses.remove(defenses);
        defenses.setShips(null);
        return this;
    }

    public Set<Cargoes> getCargoes() {
        return this.cargoes;
    }

    public void setCargoes(Set<Cargoes> cargoes) {
        if (this.cargoes != null) {
            this.cargoes.forEach(i -> i.setShips(null));
        }
        if (cargoes != null) {
            cargoes.forEach(i -> i.setShips(this));
        }
        this.cargoes = cargoes;
    }

    public Ships cargoes(Set<Cargoes> cargoes) {
        this.setCargoes(cargoes);
        return this;
    }

    public Ships addCargoes(Cargoes cargoes) {
        this.cargoes.add(cargoes);
        cargoes.setShips(this);
        return this;
    }

    public Ships removeCargoes(Cargoes cargoes) {
        this.cargoes.remove(cargoes);
        cargoes.setShips(null);
        return this;
    }

    public Set<Vehicles> getVehicles() {
        return this.vehicles;
    }

    public void setVehicles(Set<Vehicles> vehicles) {
        if (this.vehicles != null) {
            this.vehicles.forEach(i -> i.setShips(null));
        }
        if (vehicles != null) {
            vehicles.forEach(i -> i.setShips(this));
        }
        this.vehicles = vehicles;
    }

    public Ships vehicles(Set<Vehicles> vehicles) {
        this.setVehicles(vehicles);
        return this;
    }

    public Ships addVehicles(Vehicles vehicles) {
        this.vehicles.add(vehicles);
        vehicles.setShips(this);
        return this;
    }

    public Ships removeVehicles(Vehicles vehicles) {
        this.vehicles.remove(vehicles);
        vehicles.setShips(null);
        return this;
    }

    public Set<Engines> getEngines() {
        return this.engines;
    }

    public void setEngines(Set<Engines> engines) {
        if (this.engines != null) {
            this.engines.forEach(i -> i.setShips(null));
        }
        if (engines != null) {
            engines.forEach(i -> i.setShips(this));
        }
        this.engines = engines;
    }

    public Ships engines(Set<Engines> engines) {
        this.setEngines(engines);
        return this;
    }

    public Ships addEngines(Engines engines) {
        this.engines.add(engines);
        engines.setShips(this);
        return this;
    }

    public Ships removeEngines(Engines engines) {
        this.engines.remove(engines);
        engines.setShips(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ships)) {
            return false;
        }
        return getId() != null && getId().equals(((Ships) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ships{" +
            "id=" + getId() +
            ", shipId=" + getShipId() +
            ", userId=" + getUserId() +
            ", tl='" + getTl() + "'" +
            ", tonnage=" + getTonnage() +
            ", conf='" + getConf() + "'" +
            ", code='" + getCode() + "'" +
            ", hull=" + getHull() +
            ", structure=" + getStructure() +
            ", sections=" + getSections() +
            ", capital='" + getCapital() + "'" +
            ", military='" + getMilitary() + "'" +
            "}";
    }
}
