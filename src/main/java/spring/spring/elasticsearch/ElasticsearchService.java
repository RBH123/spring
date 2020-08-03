package spring.spring.elasticsearch;

import org.elasticsearch.common.xcontent.XContentBuilder;

import java.util.List;
import java.util.Map;

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

    /**
     * 根据条件查询数据
     *
     * @param tClass
     * @param condition
     * @param <T>
     * @return
     */
    <T> T queryDataOne(Class<T> tClass, String condition);

    /**
     * 批量查询数据
     *
     * @param tClass
     * @param conditions
     * @param <T>
     * @return
     */
    <T> List<T> queryDataList(Class<T> tClass, Map<String, Object> conditions);
}
