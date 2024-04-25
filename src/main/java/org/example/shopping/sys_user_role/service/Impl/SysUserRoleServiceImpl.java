package org.example.shopping.sys_user_role.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.shopping.sys_role.entity.SysRole;
import org.example.shopping.sys_user_role.entity.SysUserRole;
import org.example.shopping.sys_user_role.mapper.SysUserRoleMapper;
import org.example.shopping.sys_user_role.service.SysUserRoleService;
import org.springframework.stereotype.Service;

/**
 * @author zyr
 * @date 2024/4/11 下午10:37
 * @Description
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService{
    @Override
    public void savaRole(SysRole sysRole) {

    }
}
