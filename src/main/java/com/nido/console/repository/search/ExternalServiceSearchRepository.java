package com.nido.console.repository.search;

import com.nido.console.domain.ExternalService;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ExternalService entity.
 */
public interface ExternalServiceSearchRepository extends ElasticsearchRepository<ExternalService, Long> {
}
