package com.nido.console.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ServiceAccess.
 */
@Entity
@Table(name = "service_access")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "serviceaccess")
public class ServiceAccess implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "rate_limit")
    private Integer rateLimit;

    @Column(name = "methods")
    private String methods;

    @ManyToOne
    private ClientApp clientApp;

    @ManyToOne
    private ExternalService externalService;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ClientApp getClientApp() {
        return clientApp;
    }

    public void setClientApp(ClientApp clientApp) {
        this.clientApp = clientApp;
    }

    public ExternalService getExternalService() {
        return externalService;
    }

    public void setExternalService(ExternalService externalService) {
        this.externalService = externalService;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceAccess serviceAccess = (ServiceAccess) o;
        if(serviceAccess.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, serviceAccess.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ServiceAccess{" +
            "id=" + id +
            ", rateLimit='" + rateLimit + "'" +
            ", methods='" + methods + "'" +
            '}';
    }
}
