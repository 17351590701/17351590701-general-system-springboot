package org.example.lbspringboot.sys_category.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import org.example.lbspringboot.sys_category.entity.SysCategory;
import org.example.lbspringboot.sys_category.entity.SysCategoryPage;
import org.example.lbspringboot.sys_category.service.Impl.SysSysCategoryService;
import org.example.lbspringboot.sys_category.service.SysCategoryService;
import org.example.lbspringboot.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zyr
 * @date 2024/4/22 上午8:08
 * @Description 商品分类
 */
@RestController
@RequestMapping("/sys/category")
public class SysCategoryController {
    @Resource
    private SysCategoryService sysCategoryService;
    @Autowired
    private SysSysCategoryService sysSysCategoryService;

    //新增
    @PostMapping
    public Result add(@RequestBody SysCategory sysCategory){
        sysCategoryService.save(sysCategory);
        return Result.success("新增成功");
    }

    //删除
    @DeleteMapping("/{categoryId}")
    public Result delete(@PathVariable Long categoryId){
        sysSysCategoryService.removeById(categoryId);
        return Result.success("删除成功");
    }
    //修改
    @PutMapping
    public Result edit(@RequestBody SysCategory sysCategory){
        sysCategoryService.updateById(sysCategory);
        return Result.success("修改成功");
    }
    //查询与获取商品列表
    @GetMapping
    public Result getList(SysCategoryPage param){
        //构造分页对象
        IPage<SysCategory> page = new Page<>(param.getCurrentPage(),param.getPageSize());
        QueryWrapper<SysCategory> query = new QueryWrapper<>();
        //如果查询中商品名不为空
        if(StringUtils.isNotEmpty(param.getCategoryName())){
            query.lambda().like(SysCategory::getCategoryName,param.getCategoryName());
        }
        query.lambda().orderByDesc(SysCategory::getCreateTime);
        IPage<SysCategory> list = sysCategoryService.page(page, query);
        return Result.success("查询成功",list);

    }



}
