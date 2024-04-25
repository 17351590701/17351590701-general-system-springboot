package org.example.shopping.sys_user.entity;

import lombok.Data;

/**
 * @author zyr
 * @date 2024/4/18 下午7:19
 * @Description
 */
@Data
public class UpdatePasswordParam {
    private Long userId;
    private String oldPassword;
    private String newPassword;
}
