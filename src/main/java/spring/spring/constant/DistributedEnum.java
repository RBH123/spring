package spring.spring.constant;

/**
 * All rights Reserved, Designed By www.shengyefinance.com
 *
 * @Desc：
 * @Package：spring.spring.constant
 * @Title: DistributedEnum
 * @Author: rocky.ruan
 * @Date: 2020/8/3 16:47
 * @Copyright: 2018 www.shengyefinance.com Inc. All rights reserved.
 */
public enum DistributedEnum {

    ZK_DISTRIBUTED("/ZK-DISTBIBUTED", "zk分布式锁master节点"),
    LOCK("LOCK", "zk分布式锁临时顺序节点开端"),
    PRE_CHILDREN_NODE("/ZK-DISTBIBUTED/LOCK", "子节点前缀");

    DistributedEnum(String code, String msg) {
        this.code = code;
        this.message = msg;
    }

    private String code;

    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
