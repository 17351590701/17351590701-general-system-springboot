package org.example.shopping.sys_user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.shopping.sys_menu.entity.AssignTreeParam;
import org.example.shopping.sys_menu.entity.AssignTreeVo;
import org.example.shopping.sys_user.entity.SysUser;

/**
 * @author zyr
 * @date 2024/4/11 下午5:02
 * @Description
 */
public interface SysUserService extends IService<SysUser> {
    //新增
    void saveUser(SysUser sysUser);
    //编辑
    void editUser(SysUser sysUser);
    //删除
    void deleteUser(Long userId);
    //查询菜单树
    AssignTreeVo getAssignTree(AssignTreeParam param);
    //根据用户名查询
    SysUser loadUser(String username);
}
