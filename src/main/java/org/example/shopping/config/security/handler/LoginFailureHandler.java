package org.example.shopping.config.security.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.shopping.config.security.exception.CustomerAuthenionException;
import org.example.shopping.utils.Result;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author zyr
 * @date 2024/5/2 下午4:53
 * @Description 认证失败处理
 */
@Component
public class LoginFailureHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        int code = 500;
        String str= "";
        if (e instanceof AccountExpiredException) {
            str = "账户过期，登录失败";
        } else if (e instanceof BadCredentialsException) {
            str = "用户名或密码错误，登录失败";
        } else if (e instanceof CredentialsExpiredException) {
            str = "密码过期，登录失败";
        } else if (e instanceof DisabledException) {
            str = "账户被禁用，登录失败";
        } else if (e instanceof LockedException) {
            str = "账户锁定，登录失败";
        }else if (e instanceof InternalAuthenticationServiceException) {
            str = "用户名错误或不存在，登录失败";
        }else if (e instanceof CustomerAuthenionException) {
            code=600;
            str = e.getMessage();
        }else if (e instanceof InsufficientAuthenticationException) {
            str = "无权限访问资源";
        } else {
            str = "登录失败!";
        }
        String res = JSONObject.toJSONString(new Result(code,str,null), SerializerFeature.DisableCircularReferenceDetect);
        response.setContentType("application/json;charset=utf-8");
        ServletOutputStream out = response.getOutputStream();
        out.write(res.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }
}
