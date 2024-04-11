package org.example.lbspringboot.sys_user.entity;

import lombok.Data;

/**
 * @author zyr
 * @date 2024/4/11 下午5:12
 * @Description
 */
@Data
public class SysUserPage {
    //当前第几页
    private Long currentPage;
    //每页查询的条数
    private Long pageSize;
    //角色名称
    private String nickName;
    //电话
    private String phone;
}
