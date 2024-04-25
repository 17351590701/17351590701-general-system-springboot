package org.example.shopping.sys_role_menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.shopping.sys_role_menu.entity.RoleMenu;
import org.example.shopping.sys_role_menu.entity.SaveMenuParam;

/**
 * @author zyr
 * @date 2024/4/18 上午10:34
 * @Description
 */
public interface SysRoleMenuService extends IService<RoleMenu> {
    void saveRoleMenu(SaveMenuParam param);
}
