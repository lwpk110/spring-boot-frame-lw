package cn.tendata.mdcs.data.elasticsearch.config;

import java.util.UUID;

import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import cn.tendata.mdcs.data.elasticsearch.core.CustomEntityMapper;

@Configuration
@EnableElasticsearchRepositories(basePackages="cn.tendata.mdcs.data.elasticsearch.repository")
public class ElasticsearchConfig {

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        ImmutableSettings.Builder settings = ImmutableSettings.settingsBuilder()
                .put("http.enabled", "false")
                .put("path.data", "build/elasticsearch-test-data");
        return new ElasticsearchTemplate(
                NodeBuilder.nodeBuilder().settings(settings).clusterName(UUID.randomUUID().toString()).local(true).node().client(), 
                new CustomEntityMapper());
    }
}
