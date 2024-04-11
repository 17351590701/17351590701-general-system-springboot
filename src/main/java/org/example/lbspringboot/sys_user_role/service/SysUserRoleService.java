package org.example.lbspringboot.sys_user_role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.lbspringboot.sys_role.entity.SysRole;
import org.example.lbspringboot.sys_user_role.entity.SysUserRole;

/**
 * @author zyr
 * @date 2024/4/11 下午10:36
 * @Description
 */
public interface SysUserRoleService extends IService<SysUserRole> {
    //保存角色，用于与用户关联
    void savaRole(SysRole sysRole);
}
