package com.nido.console.repository.search;

import com.nido.console.domain.GatewayModule;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the GatewayModule entity.
 */
public interface GatewayModuleSearchRepository extends ElasticsearchRepository<GatewayModule, Long> {
}
