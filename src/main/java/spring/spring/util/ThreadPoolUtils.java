package spring.spring.util;

import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * All rights Reserved, Designed By www.shengyefinance.com
 *
 * @Desc：
 * @Package：spring.spring.util
 * @Title: ThreadPoolUtils
 * @Author: rocky.ruan
 * @Date: 2020/8/4 13:30
 * @Copyright: 2018 www.shengyefinance.com Inc. All rights reserved.
 */
public class ThreadPoolUtils {

    private static ThreadPoolExecutor threadPoolExecutor;

    private static ExecutorService executorService;

    /**
     * 拿到线程池
     *
     * @return
     */
    public static ThreadPoolExecutor getInstance() {
        threadPoolExecutor = new ThreadPoolExecutor(10, 10, 0, TimeUnit.MINUTES, new LinkedBlockingDeque<>());
        return threadPoolExecutor;
    }

    @PostConstruct
    public void init() {
        executorService = getInstance();
    }

    @SneakyThrows
    public <T> T execute(Callable<T> callable) {
        Future<T> future = executorService.submit(callable);
        return future.get();
    }

    @SneakyThrows
    public <T> List<T> executeAll(List<Callable<T>> callableList) {
        List<Future<T>> futures = executorService.invokeAll(callableList);
        if (CollectionUtils.isEmpty(futures)) {
            return Collections.EMPTY_LIST;
        }
        List<T> resultList = futures.stream().map(f -> {
            try {
                T t = f.get(5, TimeUnit.SECONDS);
                return t;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).filter(f -> f != null).collect(Collectors.toList());
        return resultList;
    }
}
