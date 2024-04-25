package org.example.shopping.sys_role_menu.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.shopping.sys_role_menu.entity.RoleMenu;
import org.example.shopping.sys_role_menu.entity.SaveMenuParam;
import org.example.shopping.sys_role_menu.mapper.SysRoleMenuMapper;
import org.example.shopping.sys_role_menu.service.SysRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zyr
 * @date 2024/4/18 上午10:35
 * @Description
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, RoleMenu> implements SysRoleMenuService {

    @Override
    @Transactional
    public void saveRoleMenu(SaveMenuParam param) {
        //先删除
        QueryWrapper<RoleMenu> query = new QueryWrapper<>();
        query.lambda().eq(RoleMenu::getRoleId,param.getRoleId());
        this.baseMapper.delete(query);
        //再保存
        this.baseMapper.saveRoleMenu(param.getRoleId(),param.getList());
    }
}
