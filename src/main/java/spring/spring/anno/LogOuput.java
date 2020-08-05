package spring.spring.anno;

import java.lang.annotation.*;

/**
 * All rights Reserved, Designed By www.shengyefinance.com
 *
 * @Desc：
 * @Package：spring.spring.anno
 * @Title: LogOuput
 * @Author: rocky.ruan
 * @Date: 2020/8/5 11:53
 * @Copyright: 2018 www.shengyefinance.com Inc. All rights reserved.
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.METHOD)
@Documented
public @interface LogOuput {
}
