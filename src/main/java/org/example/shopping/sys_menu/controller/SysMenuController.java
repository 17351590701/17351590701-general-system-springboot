package org.example.shopping.sys_menu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.shopping.sys_menu.entity.MakeMenuTree;
import org.example.shopping.sys_menu.entity.RouterVo;
import org.example.shopping.sys_menu.entity.SysMenu;
import org.example.shopping.sys_menu.service.SysMenuService;
import org.example.shopping.sys_user.entity.SysUser;
import org.example.shopping.sys_user.service.SysUserService;
import org.example.shopping.utils.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author zyr
 * @date 2024/4/13 下午8:09
 * @Description
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/sysMenu")
public class SysMenuController {
    @Resource
    private SysMenuService sysMenuService;
    @Resource
    private SysUserService sysUserService;

    // 新增
    @PostMapping
    @PreAuthorize("hasAuthority('sys:menu:add')")
    public Result add(@RequestBody SysMenu sysMenu) {
        sysMenu.setCreateTime(new Date());
        if (sysMenuService.save(sysMenu)) {
            return Result.success("新增成功");
        }
        return Result.error("新增失败");
    }

    // 编辑
    @PutMapping
    @PreAuthorize("hasAuthority('sys:menu:edit')")
    public Result edit(@RequestBody SysMenu sysMenu) {
        sysMenu.setUpdateTime(new Date());
        if (sysMenuService.updateById(sysMenu)) {
            return Result.success("编辑成功");
        }
        return Result.error("编辑失败");
    }

    // 删除
    @DeleteMapping("/{menuId}")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    public Result delete(@PathVariable("menuId") Long menuId) {
        // 如果存在下级，就不能删除
        QueryWrapper<SysMenu> query = new QueryWrapper<>();
        // 有父级Id等于此Id，说明有下级菜单
        query.lambda().eq(SysMenu::getParentId, menuId);
        if (sysMenuService.count(query) > 0) {
            return Result.error("存在下级菜单，不能删除");
        }
        if (sysMenuService.removeById(menuId)) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    // 获取菜单列表
    @GetMapping("/getList")
    public Result getList() {
        // 按序号升序
        QueryWrapper<SysMenu> query = new QueryWrapper<>();
        query.lambda().orderByAsc(SysMenu::getOrderNum);
        // 查询出所有的菜单
        List<SysMenu> list = sysMenuService.list(query);
        // 组装树结构。0L为顶级菜单
        List<SysMenu> menuList = MakeMenuTree.makeTree(list, 0L);
        return Result.success("查询成功", menuList);
    }

    // 查询上级菜单
    @GetMapping("/getParent")
    public Result getParent() {
        List<SysMenu> list = sysMenuService.getParent();
        return Result.success("查询成功", list);
    }

    //获取动态路由菜单
    @GetMapping("/getMenuList")
    public Result getMenuList(Long userId){
        //获取用户信息
        SysUser user = sysUserService.getById(userId);
        List<SysMenu> menuList =null;
        //判断是否是超级管理员
        if(StringUtils.isNotEmpty(user.getIsAdmin())&&"1".equals(user.getIsAdmin())){
            QueryWrapper<SysMenu> query = new QueryWrapper<>();
            query.lambda().orderByAsc(SysMenu::getOrderNum);
            menuList = sysMenuService.list(query);
        }else{
            menuList = sysMenuService.getMenuByUserId(user.getUserId());
        }
        //过滤菜单数据，去除按钮
        List<SysMenu> list = Optional.ofNullable(menuList)
                .orElse(Collections.emptyList())
                .stream()
                .filter(item -> item!=null&& !"2".equals(item.getType()))
                .toList();

        //组装路由数据
        List<RouterVo> routerVos = MakeMenuTree.makeRouter(list, 0L);
        return Result.success("查询成功",routerVos);
    }

}
