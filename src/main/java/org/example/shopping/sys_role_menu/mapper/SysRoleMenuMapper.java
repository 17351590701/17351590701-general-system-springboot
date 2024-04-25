package org.example.shopping.sys_role_menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.shopping.sys_role_menu.entity.RoleMenu;

import java.util.List;

/**
 * @author zyr
 * @date 2024/4/18 上午10:27
 * @Description
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<RoleMenu> {
    //保存菜单角色
    void saveRoleMenu(@Param("roleId")Long roleId, @Param("menuIds")List<Long>menuIds);

}
