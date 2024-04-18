package org.example.lbspringboot.sys_role_menu.entity;

/**
 * @author zyr
 * @date 2024/4/18 上午10:24
 * @Description
 */

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_role_menu")
public class RoleMenu {
    @TableId(type= IdType.AUTO)
    private Long roleMenuId;
    private Long roleId;
    private Long menuId;
}
