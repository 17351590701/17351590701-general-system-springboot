package org.example.shopping.config.security.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.shopping.utils.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author zyr
 * @date 2024/5/2 下午5:07
 * @Description 用户无权限访问处理
 */
@Component
public class CustomerAccessDeineHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String res = JSONObject.toJSONString(new Result(700,"无权限访问，请联系管理员",null), SerializerFeature.DisableCircularReferenceDetect);
        response.setContentType("application/json;charset=utf-8");
        ServletOutputStream out = response.getOutputStream();
        out.write(res.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }
}
