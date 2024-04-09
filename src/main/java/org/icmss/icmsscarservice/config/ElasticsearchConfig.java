package org.icmss.icmsscarservice.config;

import org.icmss.icmsscarservice.util.LocalDateTimeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;

import java.util.List;

@Configuration
public class ElasticsearchConfig extends ElasticsearchConfigurationSupport {

    @Override
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
        return new ElasticsearchCustomConversions(
                List.of(new LocalDateTimeConverter()));
    }
}
