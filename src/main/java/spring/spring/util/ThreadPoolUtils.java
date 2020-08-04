package spring.spring.util;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    public static ThreadPoolExecutor getInstance() {
        threadPoolExecutor = new ThreadPoolExecutor(10, 10, 0, TimeUnit.MINUTES, new LinkedBlockingDeque<>());
        return threadPoolExecutor;
    }
}
