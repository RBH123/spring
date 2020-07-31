package spring.spring.constant;

/**
 * All rights Reserved, Designed By www.shengyefinance.com
 *
 * @Desc：
 * @Package：spring.spring.constant
 * @Title: ElasticsearchIndexEnum
 * @Author: rocky.ruan
 * @Date: 2020/7/31 9:46
 * @Copyright: 2018 www.shengyefinance.com Inc. All rights reserved.
 */
public enum ElasticsearchIndexEnum {

    DATA_INDEX("data", "测试数据");

    private String index;
    private String desc;

    ElasticsearchIndexEnum(String index, String desc) {
        this.index = index;
        this.desc = desc;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
