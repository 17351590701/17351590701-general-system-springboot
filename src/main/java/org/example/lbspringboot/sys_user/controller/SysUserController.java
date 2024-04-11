package org.example.lbspringboot.sys_user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import org.example.lbspringboot.sys_user.entity.SysUser;
import org.example.lbspringboot.sys_user.entity.SysUserPage;
import org.example.lbspringboot.sys_user.service.SysUserService;
import org.example.lbspringboot.utils.Result;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author zyr
 * @date 2024/4/11 下午5:04
 * @Description
 */
@RestController
@CrossOrigin
@RequestMapping("/api/sysUser")
public class SysUserController {
    @Resource
    private SysUserService sysUserService;

    //新增
    @PostMapping
    public Result add(@RequestBody SysUser sysUser){
        sysUser.setCreateTime(new Date());
        if(sysUserService.save(sysUser)){
            return Result.success("新增成功");
        }else{
            return Result.error("新增失败");
        }
    }

    //编辑
    @PutMapping
    public Result edit(@RequestBody SysUser sysUser){
        sysUser.setUpdateTime(new Date());
        if(sysUserService.updateById(sysUser)){
            return Result.success("编辑成功");
        }else{
            return Result.error("编辑失败");
        }
    }

    //删除
    @DeleteMapping("/{userId}")
    public Result delete(@PathVariable("userId") Long userId){
        if(sysUserService.removeById(userId)){
            return Result.success("删除成功");
        }else{
            return Result.error("删除失败");
        }
    }


    /**
     * 获取角色列表
     *
     * @param param 包含分页信息和查询条件的用户参数对象
     * @return 返回用户查询结果，包括查询状态和用户列表
     */
    @GetMapping("/getList")
    public Result getList(SysUserPage param) {
        // 构造分页对象
        IPage<SysUser> page = new Page<>(param.getCurrentPage(), param.getPageSize());
        // 初始化查询条件
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        // 如果parm中的角色名不为空，则将角色名作为查询条件，使用like操作符进行模糊查询
        if (StringUtils.isNotEmpty((param.getNickName()))) {
            query.lambda().like(SysUser::getUsername, param.getNickName());
        }
        //根据电话查询
        if(StringUtils.isNotEmpty(param.getPhone())){
            query.lambda().like(SysUser::getPhone, param.getPhone());
        }
        // 根据创建时间进行降序排序
        query.lambda().orderByDesc(SysUser::getCreateTime);
        IPage<SysUser> list = sysUserService.page(page, query);
        return Result.success("查询成功", list);
    }


}
