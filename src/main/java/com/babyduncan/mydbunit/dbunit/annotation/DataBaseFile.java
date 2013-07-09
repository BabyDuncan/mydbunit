package com.babyduncan.mydbunit.dbunit.annotation;

import java.lang.annotation.*;

/**
 * 测试需要准备的数据文件（从classpath下查找）
 * 注解的value 可以是一个文件名，也可以是一组文件名。
 * 注解可以注释在单元测试类上面，也可以注解在测试方法上面。
 * 一个测试方法的所有准备文件是 此类的数据文件和此方法的数据文件之和。
 * 数据文件的数据格式约定如下：
 * 如果是单库：
 * 数据文件格式为：
 * ${Bizname}-dependents.xml          一个典型的${Bizname} 的值为   profile1
 * 如果是分布式数据库：
 * 数据文件格式为：
 * ${Bizpattern}-dependents.xml      一个典型的${Bizpattern} 的值为   saccounts_0
 * saccounts项目的准备文件注解示例：
 * at DataBaseFile({"saccounts_0-dependents.xml", "saccounts_1-dependents.xml", "saccounts_2-dependents.xml","saccounts_3-dependents.xml"})
 *
 * @author: guohaozhao (guohaozhao116008@babyduncan-inc.com)
 * @since: 13-5-20 14:35
 */

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DataBaseFile {
    String[] value() default "";
}
