package org.example.shopping.sys_user.entity;

import lombok.Data;

/**
 * @author zyr
 * @date 2024/4/24 下午10:03
 * @Description
 */
@Data
public class ShopParam {
    private Long userId;
    private Long currentPage;
    private Long pageSize;
}
