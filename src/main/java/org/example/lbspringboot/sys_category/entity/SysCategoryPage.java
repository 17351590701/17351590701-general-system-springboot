package org.example.lbspringboot.sys_category.entity;

import lombok.Data;

/**
 * @author zyr
 * @date 2024/4/22 上午8:26
 * @Description
 */
@Data
public class SysCategoryPage {
    private Long categoryId;
    private String categoryName;
    private Long currentPage;
    private Long pageSize;
}
