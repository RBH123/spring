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
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import spring.spring.elasticsearch.ElasticsearchService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    public <T> T queryDataOne(Class<T> tClass, String condition) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("username", "张三"));
        SearchSourceBuilder query = SearchSourceBuilder.searchSource().query(boolQueryBuilder).from(0).size(1);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(query);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        SearchHit hit = hits[0];
        String sourceAsString = hit.getSourceAsString();
        T t = JSON.parseObject(sourceAsString, tClass);
        return t;
    }

    @Override
    @SneakyThrows
    public <T> List<T> queryDataList(Class<T> tClass, Map<String, Object> conditions) {
        Set<String> keySet = conditions.keySet();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        List<QueryBuilder> builders = boolQueryBuilder.must();
        keySet.forEach(key -> {
            if (key.equals("username")) {
                MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(key, conditions.get(key));
                builders.add(matchQueryBuilder);
                return;
            }
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(key, conditions.get(key));
            builders.add(termQueryBuilder);
        });
        SearchSourceBuilder query = SearchSourceBuilder.searchSource().query(boolQueryBuilder);
        SearchRequest request = new SearchRequest();
        request.source(query);
        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = searchResponse.getHits().getHits();
        List<SearchHit> searchHits = Arrays.asList(hits);
        List<T> tList = searchHits.stream().map(s -> {
            String source = s.getSourceAsString();
            T t = JSON.parseObject(source, tClass);
            return t;
        }).filter(u -> u != null).collect(Collectors.toList());
        return tList;
    }
}
