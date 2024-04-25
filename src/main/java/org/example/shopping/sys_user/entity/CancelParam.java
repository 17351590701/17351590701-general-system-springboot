package org.example.shopping.sys_user.entity;

import lombok.Data;

/**
 * @author zyr
 * @date 2024/4/24 下午10:58
 * @Description
 */
@Data
public class CancelParam {
    private Long userId;
    private Long goodId;
}
