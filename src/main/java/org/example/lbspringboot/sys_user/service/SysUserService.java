package org.example.lbspringboot.sys_user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.lbspringboot.sys_user.entity.SysUser;

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
}
