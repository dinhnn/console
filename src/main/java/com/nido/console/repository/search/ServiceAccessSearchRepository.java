package com.nido.console.repository.search;

import com.nido.console.domain.ServiceAccess;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ServiceAccess entity.
 */
public interface ServiceAccessSearchRepository extends ElasticsearchRepository<ServiceAccess, Long> {
}
