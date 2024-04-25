package org.example.shopping.sys_menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.shopping.sys_menu.entity.SysMenu;

import java.util.List;

/**
 * @author zyr
 * @date 2024/4/13 下午8:08
 * @Description
 */
public interface SysMenuService extends IService<SysMenu> {
    List<SysMenu> getParent();
    //根据用户id获取菜单
    List<SysMenu> getMenuByUserId(Long userId);
    //根据角色id获取菜单
    List<SysMenu> getMenuByRoleId(Long roleId);

}
