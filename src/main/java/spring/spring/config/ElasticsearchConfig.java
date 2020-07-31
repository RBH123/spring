package spring.spring.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Bean
    public RestClientBuilder restClientBuilder() {
        return RestClient.builder(new HttpHost("192.168.17.14", 9200, "http"));
    }

    @Bean
    public RestClient restClient() {
        return RestClient.builder(new HttpHost("192.168.17.14", 9200, "http")).build();
    }

    @Bean
    public RestHighLevelClient restHighLevelClient(@Autowired RestClientBuilder restClientBuilder) {
        return new RestHighLevelClient(restClientBuilder);
    }
}
