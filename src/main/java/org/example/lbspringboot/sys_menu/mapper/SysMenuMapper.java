package org.example.lbspringboot.sys_menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.lbspringboot.sys_menu.entity.SysMenu;

import java.util.List;

/**
 * @author zyr
 * @date 2024/4/13 下午8:07
 * @Description
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    //根据用户id查询菜单
    List<SysMenu> getMenuByUserId(@Param("userId") Long userId);
    //根据角色id查询菜单
    List<SysMenu> getMenuByRoleId(@Param("roleId") Long roleId);

}


