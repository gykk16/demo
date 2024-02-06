package dev.glory.demo.system.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dev.glory.demo.system.filter.servletwrapperprovider.CachedBodyHttpServletRequest;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

/**
 * Request/Response Body 를 Caching 하는 Filter
 */
@WebFilter(filterName = "ContentCachingFilter", urlPatterns = "/**")
public class ContentCachingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        CachedBodyHttpServletRequest cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest(request);
        ContentCachingResponseWrapper contentCachingResponse = new ContentCachingResponseWrapper(response);

        // ==============================
        // doFilter
        // ==============================
        filterChain.doFilter(cachedBodyHttpServletRequest, contentCachingResponse);

        ContentCachingResponseWrapper wrapper =
                WebUtils.getNativeResponse(contentCachingResponse, ContentCachingResponseWrapper.class);

        Assert.notNull(wrapper, "wrapper response is null");
        wrapper.copyBodyToResponse();
    }

}
