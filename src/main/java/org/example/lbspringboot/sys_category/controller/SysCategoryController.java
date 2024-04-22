package org.example.lbspringboot.sys_category.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import org.example.lbspringboot.sys_category.entity.SysCategory;
import org.example.lbspringboot.sys_category.entity.SysCategoryPage;
import org.example.lbspringboot.sys_category.service.SysCategoryService;
import org.example.lbspringboot.sys_role.entity.SelectItem;
import org.example.lbspringboot.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author zyr
 * @date 2024/4/22 上午8:08
 * @Description 商品分类
 */
@CrossOrigin
@RestController
@RequestMapping("/api/category")
public class SysCategoryController {
    private static final Logger log = LoggerFactory.getLogger(SysCategoryController.class);
    @Resource
    private SysCategoryService sysCategoryService;

    //新增
    @PostMapping
    public Result add(@RequestBody SysCategory sysCategory){
        sysCategory.setCreateTime(new Date());
        sysCategoryService.save(sysCategory);
        return Result.success("新增成功");
    }

    //删除
    @DeleteMapping("/{categoryId}")
    public Result delete(@PathVariable Long categoryId){
        sysCategoryService.removeById(categoryId);
        return Result.success("删除成功");
    }
    //修改
    @PutMapping
    public Result edit(@RequestBody SysCategory sysCategory){
        log.info("修改参数：{}",sysCategory);
        sysCategory.setUpdateTime(new Date());
        sysCategoryService.updateById(sysCategory);
        return Result.success("修改成功");
    }
    //查询与获取商品列表
    @GetMapping("/getList")
    public Result getList(SysCategoryPage param){
        //构造分页对象
        IPage<SysCategory> page = new Page<>(param.getCurrentPage(),param.getPageSize());
        QueryWrapper<SysCategory> query = new QueryWrapper<>();
        //如果查询中商品名不为空
        if(StringUtils.isNotEmpty(param.getCategoryName())){
            query.lambda().like(SysCategory::getCategoryName,param.getCategoryName());
        } //如果查询中类型描述不为空
        if(StringUtils.isNotEmpty(param.getRemark())){
            query.lambda().like(SysCategory::getRemark,param.getRemark());
        }

        query.lambda().orderByDesc(SysCategory::getCreateTime);
        IPage<SysCategory> list = sysCategoryService.page(page, query);
        return Result.success("查询成功",list);
    }

    //商品类型下拉数据
    @GetMapping("/selectList")
    public Result selectList(){
        List<SysCategory> list = sysCategoryService.list();
        //返回的值
        List<SelectItem> selectItems = new ArrayList<>();
        Optional.ofNullable(list).orElse((new ArrayList<>()))
                .forEach(item->{
                    SelectItem si = new SelectItem();
                    si.setCheck(false);
                    si.setLabel(item.getCategoryName());
                    si.setValue(item.getCategoryId());
                    selectItems.add(si);
                });
        return Result.success("查询成功",selectItems);
    }

}

