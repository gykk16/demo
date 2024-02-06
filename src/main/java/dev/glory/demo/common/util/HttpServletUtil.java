package dev.glory.demo.common.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingResponseWrapper;

public class HttpServletUtil {

    private static final ObjectMapper OBJECT_MAPPER = Jackson2ObjectMapperBuilder.json().build();

    private HttpServletUtil() {
    }

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public static String getRequestParams() {
        StringBuilder requestParameters = new StringBuilder();
        getRequest().getParameterMap()
                .forEach((key, value) -> requestParameters.append(key)
                        .append("=")
                        .append(Arrays.toString(value))
                        .append(" "));

        return requestParameters.toString();
    }

    /**
     * @return request body
     */
    public static String getRequestBody() throws IOException {
        return String.valueOf(OBJECT_MAPPER.readTree(getRequest().getInputStream()));
    }

    /**
     * @return response body
     */
    public static String getResponseBody() {

        ContentCachingResponseWrapper wrapper = (ContentCachingResponseWrapper)getResponse();
        String payload = "";
        if (wrapper != null) {
            wrapper.setCharacterEncoding(StandardCharsets.UTF_8.name());
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                payload = new String(buf, 0, buf.length, StandardCharsets.UTF_8);
            }
        }
        return payload;
    }

}
