package spring.spring.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

import static spring.spring.constant.DistributedEnum.*;

/**
 * All rights Reserved, Designed By www.shengyefinance.com
 *
 * @Desc：
 * @Package：spring.spring.util
 * @Title: DistributedUtils
 * @Author: rocky.ruan
 * @Date: 2020/8/3 16:44
 * @Copyright: 2018 www.shengyefinance.com Inc. All rights reserved.
 */
@Slf4j
@Component
public class DistributedUtils {

    @Autowired
    private ZooKeeper zooKeeper;
    @Autowired
    private Jedis jedis;

    private ThreadLocal threadLocal = new ThreadLocal();

    private Watcher watcher = new Watcher() {
        @Override
        public void process(WatchedEvent watchedEvent) {
            if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
                    synchronized (this) {
                        notifyAll();
                    }
                }
            }
        }
    };

    @PostConstruct
    @SneakyThrows
    public void createPersistentNode() {
        Stat stat = zooKeeper.exists(ZK_DISTRIBUTED.getCode(), false);
        if (stat != null) {
            return;
        }
        zooKeeper.create(ZK_DISTRIBUTED.getCode(), ZK_DISTRIBUTED.getCode().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    /**
     * 分布式锁加锁
     *
     * @return
     */
    public boolean tryLock() {
        try {
            String znode = zooKeeper.create(ZK_DISTRIBUTED.getCode().concat("/").concat(LOCK.getCode()), String.valueOf(System.currentTimeMillis()).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            if (StringUtils.isBlank(znode)) {
                return false;
            }
            threadLocal.set(znode);
            return acquireLock();
        } catch (Exception e) {
            log.error("zk创建节点失败");
        }
        return false;
    }

    @SneakyThrows
    public boolean acquireLock() {
        String znode = (String) threadLocal.get();
        List<String> childrenNodes = zooKeeper.getChildren(ZK_DISTRIBUTED.getCode(), true);
        if (CollectionUtils.isEmpty(childrenNodes)) {
            return false;
        }
        List<Long> nodeNoList = childrenNodes.parallelStream().map(c -> {
            long no = Long.parseLong(c.substring(c.indexOf(LOCK.getCode()) + LOCK.getCode().length(), c.length()));
            return no;
        }).sorted().collect(Collectors.toList());
        long znodeNo = Long.parseLong(znode.substring(znode.indexOf(PRE_CHILDREN_NODE.getCode()) + PRE_CHILDREN_NODE.getCode().length(), znode.length()));
        znode = znode.replaceAll(String.valueOf(znodeNo), String.valueOf(znodeNo - 1));
        int index = nodeNoList.indexOf(znodeNo);
        if (index == 0) {
            return true;
        }
        Stat stat = zooKeeper.exists(znode, watcher);
        if (stat == null) {
            return acquireLock();
        } else {
            synchronized (watcher) {
                Stat stat1 = zooKeeper.exists(znode, watcher);
                if (stat1 == null) {
                    return acquireLock();
                }
                watcher.wait();
            }
            return acquireLock();
        }
    }

    /**
     * 释放锁
     */
    public void releaseLock() {
        try {
            String znodeName = (String) threadLocal.get();
            Stat stat = zooKeeper.exists(znodeName, false);
            if (stat != null) {
                zooKeeper.delete(znodeName, stat.getVersion());
            }
        } catch (Exception e) {
            log.error("删除节点失败");
        }
    }

    public boolean tryLock(String key, String value, int timeout) {
        String set = jedis.set(key, value, SetParams.setParams().nx().ex(timeout));
        if (StringUtils.isNotBlank(set) && "OK".equalsIgnoreCase(set)) {
            return true;
        }
        return false;
    }
}
