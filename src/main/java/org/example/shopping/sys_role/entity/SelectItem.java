package org.example.shopping.sys_role.entity;

import lombok.Data;

/**
 * @author zyr
 * @date 2024/4/11 下午10:03
 * @Description  前端多选的选项options
 */
@Data
public class SelectItem {
    private Long value;
    private String label;
    private Boolean check;

}
