package dev.glory.demo.system.aop.apidblog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * API 요청/응답 로그를 DB 에 저장하기 위한 어노테이션
 *
 * @see ApiDbLogAspect
 */
@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ApiDbLog {

}
