package org.example.shopping.sys_category_good.entity;

import lombok.Data;

import java.util.List;

/**
 * @author zyr
 * @date 2024/4/22 下午9:48
 * @Description
 */
@Data
public class SaveCategoryParam {
    private Long goodId;
    private List<Long> list;
}
