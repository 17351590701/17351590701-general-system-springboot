package org.example.lbspringboot.sys_role.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import org.example.lbspringboot.sys_role.entity.RoleParm;
import org.example.lbspringboot.sys_role.entity.SysRole;
import org.example.lbspringboot.sys_role.service.SysRoleService;
import org.example.lbspringboot.utils.Result;
import org.springframework.web.bind.annotation.*;

/**
 * @author zyr
 * @date 2024/4/8 下午10:18
 * @Description
 */
@RequestMapping("/api/role")
@RestController
public class SysRoleController {
    @Resource
    public SysRoleService sysRoleService;

    /**
     * 新增角色信息
     */
    @PostMapping
    public Result add(@RequestBody SysRole sysRole) {
        // 通过sysRoleService保存角色信息
        if (sysRoleService.save(sysRole)) {
            return Result.success("新增成功");
        }
        return Result.error("新增失败");
    }

    /**
     * 编辑系统角色信息
     */
    @PutMapping
    public Result edit(@RequestBody SysRole sysRole) {
        // 尝试根据传入的角色对象更新角色信息
        if (sysRoleService.updateById(sysRole)) {
            // 更新成功，返回成功信息
            return Result.success("编辑成功");
        }
        // 更新失败，返回失败信息
        return Result.error("编辑失败");
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{roleId")
    public Result delete(@PathVariable("roleId") Long roleId) {
        // 通过角色ID删除角色，如果删除成功则返回成功消息，否则返回失败消息
        if (sysRoleService.removeById(roleId)) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }


    /**
     * 获取角色列表
     *
     * @param parm 包含分页信息和查询条件的角色参数对象
     * @return 返回角色查询结果，包括查询状态和角色列表
     */
    @GetMapping("/getList")
    public Result getList(RoleParm parm) {
        // 初始化分页信息
        IPage<SysRole> page = new Page<>(parm.getCurrentPage(), parm.getPageSize());
        QueryWrapper<SysRole> query = new QueryWrapper<>();
        // 如果parm中的角色名不为空，则将角色名作为查询条件，使用like操作符进行模糊查询
        if (StringUtils.isNotEmpty((parm.getRoleName()))) {
            query.lambda().like(SysRole::getRoleName, parm.getRoleName());
        }
        IPage<SysRole> list = sysRoleService.page(page, query);
        return Result.success("查询成功", list);
    }

}
