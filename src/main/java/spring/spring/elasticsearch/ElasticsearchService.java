package spring.spring.elasticsearch;

import org.elasticsearch.common.xcontent.XContentBuilder;

import java.util.List;

public interface ElasticsearchService {

    /**
     * 创建索引
     */
    void createIndex(String index, XContentBuilder builder);

    /**
     * 添加数据到索引
     *
     * @param index 索引
     * @param t     数据
     * @param <T>
     */
    <T> void addData(String index, T t);

    /**
     * 批量添加数据
     *
     * @param list
     * @param <T>
     */
    <T> void addBatchData(String index, List<T> list);
}
