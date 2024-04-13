package org.example.lbspringboot.sys_menu.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.lbspringboot.sys_menu.entity.MakeMenuTree;
import org.example.lbspringboot.sys_menu.entity.SysMenu;
import org.example.lbspringboot.sys_menu.mapper.SysMenuMapper;
import org.example.lbspringboot.sys_menu.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author zyr
 * @date 2024/4/13 下午8:08
 * @Description
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    /**
     * 获取所有父级菜单
     * 该方法不接受任何参数，返回一个菜单树，其中包含所有父级（顶级）菜单。
     * 这些菜单是通过查询类型为0或1的菜单项，并组装成一个包含顶级菜单的树状结构来构建的。
     *
     * @return List<SysMenu> 返回一个包含所有父级菜单的列表
     */
    @Override
    public List<SysMenu> getParent() {
        // 初始化查询类型数组并转换为列表
        String[] type = {"0","1"};
        //返回固定大小的集合列表,不可增删，但可set修改
        List<String> strings = Arrays.asList(type);

        // 构建查询条件，查询类型为0或1的菜单项
        QueryWrapper<SysMenu> query = new QueryWrapper<>();
        query.lambda().in(SysMenu::getType,strings);

        // 执行查询，并获取结果列表
        List<SysMenu> menuList = this.baseMapper.selectList(query);

        // 组装顶级菜单项并添加到结果列表中
        SysMenu menu = new SysMenu();
        menu.setTitle("顶级菜单");
        menu.setLabel("顶级菜单");
        menu.setParentId(-1L);
        //Id为0L，故顶级菜单
        menu.setMenuId(0L);
        menu.setValue(0L);
        //将构建的父级菜单也添加到集合中
        menuList.add(menu);

        // 组装菜单树（顶级树），构建的
        return MakeMenuTree.makeTree(menuList, -1L);
    }

}
