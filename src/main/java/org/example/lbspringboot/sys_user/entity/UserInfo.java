package org.example.lbspringboot.sys_user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zyr
 * @date 2024/4/18 下午9:18
 * @Description  返回用户信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private Long userId;
    private String name;
    private Object[] permissons;
}
