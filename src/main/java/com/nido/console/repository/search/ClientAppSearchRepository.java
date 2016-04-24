package com.nido.console.repository.search;

import com.nido.console.domain.ClientApp;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ClientApp entity.
 */
public interface ClientAppSearchRepository extends ElasticsearchRepository<ClientApp, Long> {
}
