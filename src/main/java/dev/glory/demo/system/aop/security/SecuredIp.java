package dev.glory.demo.system.aop.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * IP 접근 제한 어노테이션
 *
 * @see SecuredIpAspect
 */
@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface SecuredIp {

    /**
     * 허용 IP
     * <ul>
     *     <li> {@code `*`} : 모든 IP 허용</li>
     * </ul>
     */
    String[] allowIp() default "";

}
