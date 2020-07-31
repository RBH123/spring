package spring.spring.elasticsearch.impl;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import spring.spring.elasticsearch.ElasticsearchService;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    @Resource
    RestHighLevelClient restHighLevelClient;

    @Override
    @SneakyThrows
    public void createIndex(String index, XContentBuilder builder) {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
        createIndexRequest.mapping(builder);
        restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
    }

    @Override
    @SneakyThrows
    public <T> void addData(String index, T t) {
        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.source(JSON.toJSONString(t), XContentType.JSON);
        restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    }

    @Override
    @SneakyThrows
    public <T> void addBatchData(String index, List<T> list) {
        BulkRequest bulkRequest = new BulkRequest();
        list.forEach(i -> {
            IndexRequest indexRequest = new IndexRequest(index).source(JSON.toJSONString(i), XContentType.JSON);
            bulkRequest.add(indexRequest);
        });
        restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    @Override
    @SneakyThrows
    public <T> T queryData(Class<T> tClass, String condition) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("username", "张三"));
        SearchSourceBuilder query = SearchSourceBuilder.searchSource().query(boolQueryBuilder);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(query);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        SearchHit hit = hits[0];
        String sourceAsString = hit.getSourceAsString();
        T t = JSON.parseObject(sourceAsString, tClass);
        return t;
    }
}
