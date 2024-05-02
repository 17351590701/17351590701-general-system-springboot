package org.example.shopping.config.security.filter;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.example.shopping.config.security.DBUserDetailsService;
import org.example.shopping.config.security.handler.LoginFailureHandler;
import org.example.shopping.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zyr
 * @date 2024/5/2 下午2:53
 * @Description 登录和验证码不需要验证
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component
@Slf4j
public class CheckTokenFilter extends OncePerRequestFilter {
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private DBUserDetailsService dbUserDetailsService;
    // @Resource
     private LoginFailureHandler loginFailureHandler;
    //token验证url白名单
    @Value("#{'${ignore.url}'.split(',')}")

    public List<String> ignoreUrl = Collections.emptyList();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 获取请求的url
            String url = request.getRequestURI();
            // 判断是否需要验证
            if (!ignoreUrl.contains(url)) {
                validateToken(request);
            }
        } catch (AuthenticationException e) {
             loginFailureHandler.commence(request,response,e);
             return ;
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 验证令牌（token）的有效性，并基于该token进行用户认证和设置安全上下文。
     *
     * @param request HttpServletRequest对象，用于从中提取token。
     */
    protected void validateToken(HttpServletRequest request) {
        // 尝试从请求头获取token，如果不存在则尝试从请求参数获取
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }
        // 记录token为空的情况
        if (StringUtils.isEmpty(token)) {
            log.info("token为空");
        }
        // 验证token的合法性
        if (!jwtUtils.verify(token)) {
            log.info("非法的token");
        }
        // 解析token以获取用户信息
        DecodedJWT decodedJWT = jwtUtils.jwtDecode(token);
        Map<String, Claim> claims = decodedJWT.getClaims();
        // 通过username获取用户详情
        String username = claims.get("username").asString();
        UserDetails userDetails = dbUserDetailsService.loadUserByUsername(username);
        // 创建认证令牌并设置细节
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // 将认证令牌设置到安全上下文中
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
