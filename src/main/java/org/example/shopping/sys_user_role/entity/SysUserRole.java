package org.example.shopping.sys_user_role.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author zyr
 * @date 2024/4/11 下午10:34
 * @Description
 */
@Data
public class SysUserRole {
    @TableId(type = IdType.AUTO)
    private Long user_role_id;
    private Long userId;
    private Long roleId;
}
