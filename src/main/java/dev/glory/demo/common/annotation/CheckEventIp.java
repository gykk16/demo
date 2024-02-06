package dev.glory.demo.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 이벤트 IP 체크
 * <ul>
 *     <li>해당 어노테이션을 사용하는 시그니쳐는 {@code 'eventId'(Long.class)} 파라미터가 있어야 한다</li>
 *     <li>btob_send_ip 테이블에 등록된 IP 만 허용 한다</li>
 * </ul>
 */
@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface CheckEventIp {

}
