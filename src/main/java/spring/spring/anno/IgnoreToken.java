package spring.spring.anno;

import spring.spring.constant.CheckTokenEnum;

import java.lang.annotation.*;

/**
 * @DateTime：2020/8/18 10:46
 * @ClassName：IgnoreToken
 * @PackageName：spring.spring.anno
 * @Author：Rocky.Ruan
 * @Desc：描述一下
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface IgnoreToken {
    CheckTokenEnum value() default CheckTokenEnum.CHECK_TOKEN;
}
