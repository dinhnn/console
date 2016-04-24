package com.nido.console.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ExternalService.
 */
@Entity
@Table(name = "external_service")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "externalservice")
public class ExternalService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Pattern(regexp = "undefined")
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "contact")
    private String contact;

    @NotNull
    @Column(name = "endpoint", nullable = false)
    private String endpoint;

    @NotNull
    @Column(name = "health_check", nullable = false)
    private String healthCheck;

    @Column(name = "rate_limit")
    private Integer rateLimit;

    @Column(name = "methods")
    private String methods;

    @Column(name = "version")
    private Integer version;

    @ManyToOne
    private GatewayModule module;

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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getHealthCheck() {
        return healthCheck;
    }

    public void setHealthCheck(String healthCheck) {
        this.healthCheck = healthCheck;
    }

    public Integer getRateLimit() {
        return rateLimit;
    }

    public void setRateLimit(Integer rateLimit) {
        this.rateLimit = rateLimit;
    }

    public String getMethods() {
        return methods;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public GatewayModule getModule() {
        return module;
    }

    public void setModule(GatewayModule gatewayModule) {
        this.module = gatewayModule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExternalService externalService = (ExternalService) o;
        if(externalService.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, externalService.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ExternalService{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", contact='" + contact + "'" +
            ", endpoint='" + endpoint + "'" +
            ", healthCheck='" + healthCheck + "'" +
            ", rateLimit='" + rateLimit + "'" +
            ", methods='" + methods + "'" +
            ", version='" + version + "'" +
            '}';
    }
}
