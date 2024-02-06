package dev.glory.demo.system.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * LogResponseBody
 * <p>
 * annotation to log response body information to log file
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogResponseBody {

    /**
     * maxLength
     * <p>
     * max length of response body to print
     */
    int maxLength() default 1_000;

}
