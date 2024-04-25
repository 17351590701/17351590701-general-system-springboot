package org.example.shopping.sys_category.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

/**
 * @author zyr
 * @date 2024/4/25 上午10:08
 * @Description  获取总商品数，以及各分类的商品数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EChartModel {
    private Long total;
    private HashMap<String,Long> map;
}
