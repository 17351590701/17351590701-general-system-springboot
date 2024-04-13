package org.example.lbspringboot.sys_menu.entity;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author zyr
 * @date 2024/4/13 下午8:16
 * @Description
 */
public class MakeMenuTree {
    /**
     * 构建菜单树结构
     * @param menuList 菜单列表，包含所有菜单项
     * @param pid 父菜单的ID，用于构建指定父菜单下的子菜单树
     * @return 返回构建好的菜单树结构列表
     */
    public static List<SysMenu> makeTree(List<SysMenu> menuList,Long pid) {
        List<SysMenu> list = new ArrayList<>();
        // 流式处理menuList
        Optional.ofNullable(menuList).orElse(new ArrayList<>()) //判断menuList是否为空，为空则new ArrayList
                .stream()                                       //否则
                .filter(item->item!=null&&item.getParentId().equals(pid))//过滤出父菜单ID为pid的菜单项
                .forEach(item->{
                    // 创建一个新的SysMenu对象，用于构建树结构
                    SysMenu menu = new SysMenu();
                    // 复制item的属性到menu，避免修改原数据
                    BeanUtils.copyProperties(item,menu);
                    // 设置menu的显示标签和值
                    menu.setLabel(item.getTitle());
                    menu.setValue(item.getMenuId());
                    // 递归，以自生id为父id，构建子菜单
                    List<SysMenu> children = makeTree(menuList, item.getMenuId());
                    // 设置menu的子菜单列表
                    menu.setChildren(children);
                    // 将构建好的menu添加到集合中
                    list.add(menu);
                });
        return list; // 返回构建好的菜单树
    }
}
