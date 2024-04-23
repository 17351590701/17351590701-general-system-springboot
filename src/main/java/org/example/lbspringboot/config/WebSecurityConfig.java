package org.example.lbspringboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.CrossOrigin;


/**
 * @author zyr
 * @date 2024/4/23 下午9:17
 * @Description SpringSecurity配置
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig{
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())  // 使用默认的CORS配置
                .headers(AbstractHttpConfigurer::disable) //禁用csrf保护
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/sysUser/login", "/api/sysUser/getImage").permitAll()  // 允许匿名访问登录页面和验证码生成接口
                        .anyRequest().authenticated()  // 其他请求需经过身份验证
                )
                .formLogin((form) -> form
                        .loginPage("/api/sysUser/login")  // 指定登录页面
                        .permitAll()  // 允许所有用户访问登录页面
                );
        return http.build();
    }
}
