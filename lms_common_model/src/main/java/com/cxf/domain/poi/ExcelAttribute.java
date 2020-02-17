package com.cxf.domain.poi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ExcelAttribute注解用于导入Excel表时对应列与对象属性
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelAttribute {
    /** 对应的列名称 */
    String name() default "";

    /** excel列的索引 */
    int sort();

    /** 字段类型对应的格式 */
    String format() default "";
}
