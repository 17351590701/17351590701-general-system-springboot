package org.example.shopping.sys_role.entity;

import lombok.Data;

/**
 * @author zyr
 * @date 2024/4/8 下午10:26
 * @Description
 */
@Data
public class SysRolePage {
    //当前第几页
    private Long currentPage;
    //每页查询的条数
    private Long pageSize;
    //角色名称
    private String roleName;
}
