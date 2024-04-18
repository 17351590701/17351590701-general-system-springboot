package org.example.lbspringboot.sys_role.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.lbspringboot.sys_role.entity.SelectItem;
import org.example.lbspringboot.sys_role.entity.SysRolePage;
import org.example.lbspringboot.sys_role.entity.SysRole;
import org.example.lbspringboot.sys_role.service.SysRoleService;
import org.example.lbspringboot.sys_role_menu.entity.SaveMenuParam;
import org.example.lbspringboot.sys_role_menu.service.Impl.SysRoleMenuServiceImpl;
import org.example.lbspringboot.utils.Result;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author zyr
 * @date 2024/4/8 下午10:18
 * @Description
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/role")
public class SysRoleController {
    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysRoleMenuServiceImpl sysRoleMenuService;

    // 新增
    @PostMapping
    public Result add(@RequestBody SysRole sysRole) {
        // 手动添加创建时间
        sysRole.setCreateTime(new Date());
        if (sysRoleService.save(sysRole)) {
            return Result.success("新增成功");
        }
        return Result.error("新增失败");
    }

    // 编辑
    @PutMapping
    public Result edit(@RequestBody SysRole sysRole) {
        sysRole.setUpdateTime(new Date());
        if (sysRoleService.updateById(sysRole)) {
            return Result.success("编辑成功");
        }
        return Result.error("编辑失败");
    }

    // 删除角色
    @DeleteMapping("/{roleId}")
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
     * @param param 包含分页信息和查询条件的角色参数对象
     * @return 返回角色查询结果，包括查询状态和角色列表
     */
    @GetMapping("/getList")
    public Result getList(SysRolePage param) {
        // 初始化分页信息
        IPage<SysRole> page = new Page<>(param.getCurrentPage(), param.getPageSize());
        QueryWrapper<SysRole> query = new QueryWrapper<>();
        // 如果parm中的角色名不为空，则将角色名作为查询条件，使用like操作符进行模糊查询
        if (StringUtils.isNotEmpty((param.getRoleName()))) {
            query.lambda().like(SysRole::getRoleName, param.getRoleName());
        }
        query.lambda().orderByDesc(SysRole::getCreateTime);
        IPage<SysRole> list = sysRoleService.page(page, query);
        return Result.success("查询成功", list);
    }

    //角色下拉数据列表
    @GetMapping("/selectList")
    public Result selectList(){
        List<SysRole> list = sysRoleService.list();
        //返回的值
        List<SelectItem> selectItems = new ArrayList<>();
        Optional.ofNullable(list).orElse((new ArrayList<>()))
                .forEach(item->{
                    SelectItem si = new SelectItem();
                    si.setCheck(false);
                    si.setLabel(item.getRoleName());
                    si.setValue(item.getRoleId());
                    selectItems.add(si);
                });
        return Result.success("查询成功",selectItems);
    }

    //保存角色菜单
    @PostMapping("/saveRoleMenu")
    public Result saveRoleMenu(@RequestBody SaveMenuParam param){
        sysRoleMenuService.saveRoleMenu(param);
        return Result.success("分配成功");
    }
}
