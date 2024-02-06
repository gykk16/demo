package dev.glory.demo.system.config;

import static dev.glory.demo.common.constant.WebAppConst.INTERCEPTOR_EXCLUDE_PATH;

import lombok.RequiredArgsConstructor;

import dev.glory.demo.system.interceptor.DocsSecureInterceptor;
import dev.glory.demo.system.interceptor.LogInterceptor;
import dev.glory.demo.system.interceptor.LogResponseBodyInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(docsSecureInterceptor())
                .order(1)
                .addPathPatterns("/docs/**");

        registry.addInterceptor(logInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns(INTERCEPTOR_EXCLUDE_PATH);

        registry.addInterceptor(logResponseBodyInterceptor())
                .order(3)
                .addPathPatterns("/**")
                .excludePathPatterns(INTERCEPTOR_EXCLUDE_PATH);
    }

    @Bean
    public LogInterceptor logInterceptor() {
        return new LogInterceptor();
    }

    @Bean
    public LogResponseBodyInterceptor logResponseBodyInterceptor() {
        return new LogResponseBodyInterceptor();
    }

    @Bean
    public DocsSecureInterceptor docsSecureInterceptor() {
        return new DocsSecureInterceptor();
    }

}
