package dev.glory.demo.system.aop.security;

import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import dev.glory.demo.common.code.ErrorCode;
import dev.glory.demo.common.exception.BizRuntimeException;
import dev.glory.demo.common.util.IpAddrUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

/**
 * 접근이 허용된 IP 인지 확인 하는 Aspect
 * <ul>
 *     <li>IP 검사를 할 메소드에 {@link SecuredIp} 어노테이션을 추가 한다 </li>
 * </ul>
 */
@Aspect
@Order(1)
@Slf4j
public class SecuredIpAspect {

    private static final Set<String> WHITE_LIST_IPS = Set.of(
            "0:0:0:0:0:0:0:1",
            "127.0.0.1"
    );

    @Around("@annotation(securedIp)")
    public Object securedIp(ProceedingJoinPoint joinPoint, SecuredIp securedIp) throws Throwable {
        log.info("# ==> securedIp = {}", securedIp);
        final String serverIP = IpAddrUtil.getServerIP();
        final String clientIP = IpAddrUtil.getClientRealIP();

        if (clientIP.equals(serverIP)) {
            return joinPoint.proceed();
        }

        if (WHITE_LIST_IPS.contains(clientIP)) return joinPoint.proceed();
        if (validateAnnotationIp(clientIP, securedIp.allowIp())) return joinPoint.proceed();

        throw new BizRuntimeException(ErrorCode.UNAUTHORIZED);
    }

    private static boolean validateAnnotationIp(String clientIP, String[] ipArray) {
        if (ipArray.length > 0) {
            if (ipArray[0].equals("*")) {
                return true;
            }
            for (String ip : ipArray) {
                if (ip.equals(clientIP)) {
                    return true;
                }
            }
        }
        return false;
    }

}
