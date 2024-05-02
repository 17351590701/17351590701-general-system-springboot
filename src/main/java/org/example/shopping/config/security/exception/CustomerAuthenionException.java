package org.example.shopping.config.security.exception;

import org.springframework.security.core.AuthenticationException;

import java.io.Serial;

/**
 * @author zyr
 * @date 2024/5/2 下午4:54
 * @Description 自定义认证异常
 */
public class CustomerAuthenionException extends AuthenticationException {
    @Serial
    private static final long serialVersionUID = -2860772942229373595L;

    public CustomerAuthenionException(String msg) {
        super(msg);
    }
}
