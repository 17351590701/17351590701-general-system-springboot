package org.example.shopping.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Map;

/**
 * @author zyr
 * @date 2024/4/18 下午8:28
 * @Description
 */
@Component
@Data
//获取yml配置文件中jwt前缀的配置
@ConfigurationProperties(prefix = "jwt")
public class JwtUtils {
    //颁发者
    private String issuer;
    //密钥
    private String secret;
    //过期时间，定义为86400s
    private Integer expiration;

    /**
     * 生成JWT令牌。
     *
     * @param map 用于设置JWT令牌中的claim的信息，键值对形式。
     * @return 生成的JWT令牌字符串。
     */
    public String generateToken(Map<String,String> map){
        // 获取当前时间实例，并设置令牌过期时间
        Calendar instance = Calendar.getInstance();
        // 设置令牌失效时间，基于当前时间往后推算expiration秒
        instance.add(Calendar.SECOND,expiration);
        // 创建JWT构建器
        JWTCreator.Builder builder = JWT.create();
        // 设置payload，将map中的所有键值对加入到令牌的claim中
        map.forEach(builder::withClaim);
        String token;
        // 设置令牌的发行者、过期时间，并使用秘钥签名生成最终令牌字符串
        token = builder.withIssuer(issuer).withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(secret));

        return token;
    }

    /**
     * 验证JWT(token)的有效性。
     *
     * @param token 待验证的JWT令牌。
     * @return 如果令牌验证成功，则返回true；如果验证失败，则返回false。
     */
    public boolean verify(String token){
        try{
            // 使用JWT的要求构建验证器，并基于提供的密钥进行验证
            JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
        }catch(JWTVerificationException e){
            // 如果捕获到JWT验证异常，则表示验证失败，返回false
            return false;
        }
        // 无异常捕获，表示验证通过，返回true
        return true;
    }

    /**
     * 解码验证JWT token。
     *
     * @param token 待解码的JWT字符串。
     * @return 解码后的DecodedJWT对象，包含JWT的详细信息。
     * @throws RuntimeException 当遇到签名验证失败、算法不匹配、token过期或token无效的情况时抛出。
     */
    public DecodedJWT jwtDecode(String token){
        try{
            return JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
        }catch(SignatureVerificationException e){
            throw new RuntimeException("token签名验证失败");
        }catch(AlgorithmMismatchException e){
            throw new RuntimeException("token算法不一致");
        }catch(TokenExpiredException e){
            throw new RuntimeException("token过期");
        }catch(Exception e){
            throw new RuntimeException("token无效 ");
        }
    }

}
