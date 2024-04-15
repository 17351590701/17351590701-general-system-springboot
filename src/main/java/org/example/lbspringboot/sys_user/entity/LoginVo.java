package org.example.lbspringboot.sys_user.entity;

import lombok.Data;

/**
 * @author zyr
 * @date 2024/4/15 下午10:45
 * @Description
 */
@Data
public class LoginVo {
    private Long userId;
    private String nickName;
    private String token;
}
