package org.example.shopping.sys_user.entity;

import lombok.Data;

/**
 * @author zyr
 * @date 2024/4/15 下午10:45
 * @Description
 */
@Data
public class LoginParam {
    private String username;
    private String password;
    private String code;
}

