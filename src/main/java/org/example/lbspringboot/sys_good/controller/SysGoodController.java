package org.example.lbspringboot.sys_good.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.lbspringboot.sys_category_good.entity.SysCategoryGood;
import org.example.lbspringboot.sys_category_good.service.SysCategoryGoodService;
import org.example.lbspringboot.sys_good.entity.SysGood;
import org.example.lbspringboot.sys_good.entity.SysGoodPage;
import org.example.lbspringboot.sys_good.service.SysGoodService;
import org.example.lbspringboot.utils.Result;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * @author zyr
 * @date 2024/4/22 下午1:42
 * @Description
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/good")
public class SysGoodController {
    @Resource
    private SysGoodService sysGoodService;
    @Resource
    private SysCategoryGoodService sysCategoryGoodService;
    //新增
    @PostMapping
    public Result addGood(@RequestBody SysGood sysGood){
        sysGood.setCreateTime(new Date());
        sysGoodService.saveGood(sysGood);
        return Result.success("新增成功");
}

    //删除
    @DeleteMapping("/{goodId}")
    public Result deleteGood(@PathVariable Long goodId){
        sysGoodService.deleteGood(goodId);
        return Result.success("删除成功");
    }
    //修改
    @PutMapping
    public Result updateGood(@RequestBody SysGood sysGood){
        sysGood.setUpdateTime(new Date());
        sysGoodService.editGood(sysGood);
        return Result.success("修改成功");
    }
    //查询
    @GetMapping("/getList")
    public Result getList(SysGoodPage param){
        IPage<SysGood> page = new Page<>(param.getCurrentPage(),param.getPageSize());
        QueryWrapper<SysGood> query = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(param.getGoodName())){
            query.lambda().like(SysGood::getGoodName,param.getGoodName());
        }
        if(StringUtils.isNotEmpty(param.getDescription())){
            query.lambda().like(SysGood::getDescription,param.getDescription());
        }
        IPage<SysGood> list = sysGoodService.page(page, query);
        return Result.success("查询成功",list);
    }

    //根据商品id查新对应的商品类型
    @GetMapping("/getCategoryList")
    public Result getCategoryList(Long goodId){
        QueryWrapper<SysCategoryGood> query= new QueryWrapper<>();
        query.lambda().eq(SysCategoryGood::getGoodId,goodId);
        //查询满足条件的category-good表至lisi中
        List<SysCategoryGood> list = sysCategoryGoodService.list(query);
        //categoryId集合
        List<Long> categoryList = new ArrayList<>();
        //判断查询是否为空
        Optional.ofNullable(list).orElse(new ArrayList<>())
                .forEach(item->{
                    categoryList.add(item.getCategoryId());
                });
        return Result.success("查询成功",categoryList);
    }

}
