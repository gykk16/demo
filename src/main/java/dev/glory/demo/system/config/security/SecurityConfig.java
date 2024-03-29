package dev.glory.demo.system.config.security;

import static dev.glory.demo.domain.user.role.Permission.ADMIN_READ;
import static dev.glory.demo.domain.user.role.Permission.MANAGER_READ;
import static dev.glory.demo.domain.user.role.Role.ADMIN;
import static dev.glory.demo.domain.user.role.Role.MANAGER;
import static dev.glory.demo.domain.user.role.Role.SUPER;
import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.RequiredArgsConstructor;

import dev.glory.demo.system.config.security.jwt.JwtAccessDeniedHandler;
import dev.glory.demo.system.config.security.jwt.JwtAuthenticationEntryPoint;
import dev.glory.demo.system.filter.jwt.JwtAuthenticationFilter;
import dev.glory.demo.system.filter.trace.TraceKeyFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TraceKeyFilter          traceKeyFilter;
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider  authenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                        .accessDeniedHandler(new JwtAccessDeniedHandler())
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**")
                        .permitAll()

                        .requestMatchers("/docs/**")
                        .permitAll()

                        .requestMatchers("/api/v1/demo/manager/**")
                        .hasAnyRole(SUPER.name(), ADMIN.name(), MANAGER.name())

                        .requestMatchers(GET, "/api/v1/demo/manager/**")
                        .hasAnyAuthority(SUPER.name(), ADMIN_READ.name(), MANAGER_READ.name())

                        .requestMatchers("/api/v1/notice/**")
                        .hasAnyRole(SUPER.name(), ADMIN.name(), MANAGER.name())

                        // .requestMatchers("/api/v1/demo/admin/**")
                        // .hasAnyRole(SUPER.name(), ADMIN.name())
                        //
                        // .requestMatchers(GET, "/api/v1/demo/admin/**")
                        // .hasAnyAuthority(SUPER.name(), ADMIN_READ.name())

                        .anyRequest()
                        .authenticated()
                )

                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))

                .authenticationProvider(authenticationProvider)
                .addFilterBefore(traceKeyFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public WebSecurityCustomizer configure() {
        return web -> web.ignoring()
                .requestMatchers(toH2Console());
    }
}
