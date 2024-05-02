package org.example.shopping.config.security;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.shopping.config.security.filter.CheckTokenFilter;
import org.example.shopping.config.security.handler.CustomerAccessDeineHandler;
import org.example.shopping.config.security.handler.LoginFailureHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * @author zyr
 * @date 2024/5/1 下午11:12
 * @Description
 */
@Slf4j
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
    @Resource
    private CheckTokenFilter checkTokenFilter;
    // 过滤器链
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/sysUser/login",
                                "/api/sysUser/getImage"
                        ).permitAll()
                        .anyRequest()
                        .authenticated()
                );
        http
                .exceptionHandling(e->{
                    //未认证返回登录页
                    e.authenticationEntryPoint(new LoginFailureHandler());
                    //无权限访问处理
                    e.accessDeniedHandler(new CustomerAccessDeineHandler());
                });
        // token过滤器
        http.addFilterBefore(checkTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 禁用浏览器的同源策略限制
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        // 全局跨域
        http.cors(Customizer.withDefaults());
        // 基于token，不需要 csrf
        http.csrf(AbstractHttpConfigurer::disable);
        // 基于token，不需要 session
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    // 获取AuthenticationManager（认证管理器），登录时认证使用
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 密码解析器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
