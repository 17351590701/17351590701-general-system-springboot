package org.example.lbspringboot.sys_menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.lbspringboot.sys_menu.entity.SysMenu;

import java.util.List;

/**
 * @author zyr
 * @date 2024/4/13 下午8:08
 * @Description
 */
public interface SysMenuService extends IService<SysMenu> {
    List<SysMenu> getParent();
}
