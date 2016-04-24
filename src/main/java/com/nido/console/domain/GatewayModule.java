package com.nido.console.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A GatewayModule.
 */
@Entity
@Table(name = "gateway_module")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "gatewaymodule")
public class GatewayModule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Size(max = 100)
    @Pattern(regexp = "undefined")
    @Column(name = "artifact", length = 100, nullable = false)
    private String artifact;

    @Column(name = "activated")
    private Boolean activated;

    @NotNull
    @Column(name = "instances", nullable = false)
    private Integer instances;

    @Column(name = "settings")
    private String settings;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArtifact() {
        return artifact;
    }

    public void setArtifact(String artifact) {
        this.artifact = artifact;
    }

    public Boolean isActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Integer getInstances() {
        return instances;
    }

    public void setInstances(Integer instances) {
        this.instances = instances;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GatewayModule gatewayModule = (GatewayModule) o;
        if(gatewayModule.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, gatewayModule.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "GatewayModule{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", artifact='" + artifact + "'" +
            ", activated='" + activated + "'" +
            ", instances='" + instances + "'" +
            ", settings='" + settings + "'" +
            '}';
    }
}
