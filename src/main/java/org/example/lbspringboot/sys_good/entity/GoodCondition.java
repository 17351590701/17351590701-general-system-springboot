package org.example.lbspringboot.sys_good.entity;

import lombok.Data;

/**
 * @author zyr
 * @date 2024/4/23 下午2:54
 * @Description 按分类和价格区间查询
 */
@Data
public class GoodCondition {
    String categoryIds;
    String priceMin;
    String priceMax;
}
