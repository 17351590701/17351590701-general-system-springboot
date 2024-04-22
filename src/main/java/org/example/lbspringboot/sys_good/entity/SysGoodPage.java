package org.example.lbspringboot.sys_good.entity;

import lombok.Data;

/**
 * @author zyr
 * @date 2024/4/22 下午1:45
 * @Description
 */
@Data
public class SysGoodPage {
    private String goodName;
    private String description;
    private Long price;
    private Long currentPage;
    private Long pageSize;
}
