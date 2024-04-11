package org.example.lbspringboot.sys_user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.lbspringboot.sys_user.entity.SysUser;
import org.example.lbspringboot.sys_user.mapper.SysUserMapper;
import org.example.lbspringboot.sys_user.service.SysUserService;
import org.example.lbspringboot.sys_user_role.entity.SysUserRole;
import org.example.lbspringboot.sys_user_role.service.SysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zyr
 * @date 2024/4/11 下午5:02
 * @Description
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Resource
    private SysUserRoleService sysUserRoleService;

    /**
     * 保存用户信息，如果用户角色ID存在，则同时保存用户角色关系。
     *
     * @param sysUser 用户实体对象，包含用户基本信息和角色ID。
     */
    @Override
    @Transactional
    public void saveUser(SysUser sysUser) {
        // 尝试插入用户信息
        int i = this.baseMapper.insert(sysUser);
        // 如果插入成功（影响行数大于0），则处理用户角色关系
        if (i > 0) {
            // 用户角色ID以逗号分隔，此处将其分割为数组
            String[] split = sysUser.getRoleId().split(",");
            // 保存用户与角色的关系
            InsertUserRole(sysUser, split);
        }

    }
    /**
     * 编辑用户信息及其角色关系。
     *
     * @param sysUser 用户实体对象，包含用户信息和角色ID。
     *                该方法首先尝试更新用户信息，如果更新成功，则删除该用户原有的角色关系，并重新建立新的角色关系。
     */
    @Override
    @Transactional
    public void editUser(SysUser sysUser) {
        // 尝试根据用户ID更新用户信息
        int i = this.baseMapper.updateById(sysUser);
        if (i > 0) {
            // 根据角色ID字符串，分割出角色ID数组
            String[] split = sysUser.getRoleId().split(",");
            // 构造查询Wrapper，用于删除用户原有角色关系
            QueryWrapper<SysUserRole> query = new QueryWrapper<>();
            query.lambda().eq(SysUserRole::getUserId, sysUser.getUserId());
            // 删除用户与角色的原有关系
            sysUserRoleService.remove(query);
            // 建立用户与角色的新关系
            InsertUserRole(sysUser, split);
        }

    }

    /**
     * 删除用户信息及其关联的角色信息
     *
     * @param userId 用户ID
     *               删除用户信息时，若该用户存在，则同时删除该用户所有关联的角色信息。
     */
    @Override
    public void deleteUser(Long userId) {
        // 通过用户ID删除用户信息，返回值为删除的行数
        int i = this.baseMapper.deleteById(userId);
        // 当删除的行数大于0，即用户存在时，删除该用户所有关联的角色信息
        if (i > 0) {
            QueryWrapper<SysUserRole> query = new QueryWrapper<>();
            // 构造查询条件，根据用户ID删除用户角色关联信息
            query.lambda().eq(SysUserRole::getUserId, userId);
            sysUserRoleService.remove(query);
        }
    }
    /**
     * 插入用户角色关系。
     * 该方法用于将一个用户与多个角色建立关系，通过解析角色ID字符串数组来实现。
     *
     * @param sysUser 表示一个系统用户对象，该用户需要被赋予角色。
     * @param split   一个字符串数组，包含需要被赋予给用户的角色ID字符串。每个字符串代表一个角色ID。
     */
    private void InsertUserRole(SysUser sysUser, String[] split) {
        if (split.length > 0) {
            // 初始化一个空的角色列表，用于存储用户角色关系数据。
            List<SysUserRole> roles = new ArrayList<>();
            for (String s : split) {
                // 为每个角色ID创建一个用户角色关系对象，并设置用户ID和角色ID。
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(sysUser.getUserId());
                userRole.setRoleId(Long.parseLong(s));
                roles.add(userRole); // 将用户角色关系对象添加到角色列表中。
            }
            // 批量保存用户角色关系到数据库。
            sysUserRoleService.saveBatch(roles);
        }
    }
}
