package org.example.lbspringboot.sys_category.entity;

import lombok.Data;

/**
 * @author zyr
 * @date 2024/4/22 上午8:26
 * @Description
 */
@Data
public class SysCategoryPage {
    private String categoryName;
    private String remark;
    private Long currentPage;
    private Long pageSize;
}
