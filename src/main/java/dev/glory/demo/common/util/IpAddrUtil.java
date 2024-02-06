package dev.glory.demo.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
public class IpAddrUtil {

    private static final String SERVER_IP;
    private static final String SERVER_IP_LAST_OCTET;

    static {
        String serverIp;
        try {
            serverIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ignored) {
            log.warn("==> UnknownHostException");
            serverIp = "unknown";
        }
        SERVER_IP = serverIp;
        SERVER_IP_LAST_OCTET = serverIp.substring(serverIp.lastIndexOf(".") + 1);
    }

    private IpAddrUtil() {
    }

    public static String getServerIP() {
        return SERVER_IP;
    }

    public static String getServerIpLastOctet() {
        return SERVER_IP_LAST_OCTET;
    }

    public static String getClientIP() {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        return getClientIP(request);
    }

    public static String getClientIP(HttpServletRequest request) {
        String[] ipHeaderCandidates = {"X-Forwarded-For", "Proxy-Client-IP",
                "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR"};

        for (String header : ipHeaderCandidates) {
            String ip = request.getHeader(header);
            if (StringUtils.hasText(ip) && !StringUtils.startsWithIgnoreCase(ip, "unknown")) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

    public static String getClientRealIP() {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        return getClientRealIP(request);
    }

    public static String getClientRealIP(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

}
