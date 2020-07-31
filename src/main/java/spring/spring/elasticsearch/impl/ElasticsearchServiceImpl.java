package spring.spring.elasticsearch.impl;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
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
}
