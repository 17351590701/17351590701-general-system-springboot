package org.example.lbspringboot.sys_good.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.lbspringboot.sys_category.service.SysCategoryService;
import org.example.lbspringboot.sys_category_good.entity.SysCategoryGood;
import org.example.lbspringboot.sys_category_good.service.SysCategoryGoodService;
import org.example.lbspringboot.sys_good.entity.GoodCondition;
import org.example.lbspringboot.sys_good.entity.SysGood;
import org.example.lbspringboot.sys_good.mapper.SysGoodMapper;
import org.example.lbspringboot.sys_good.service.SysGoodService;
import org.example.lbspringboot.sys_user_role.entity.SysUserRole;
import org.example.lbspringboot.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zyr
 * @date 2024/4/22 下午1:41
 * @Description
 */
@Service
public class SysGoodServiceImpl extends ServiceImpl<SysGoodMapper, SysGood> implements SysGoodService {
    @Resource
    private SysCategoryGoodService sysCategoryGoodService;

    @Override
    @Transactional
    public void saveGood(SysGood sysGood) {
        // 尝试插入商品信息
        int i = this.baseMapper.insert(sysGood);
        // 如果插入成功，则处理商品和分类的关系
        if (i > 0) {
            // 分类id以逗号分隔为数组
            String[] split = sysGood.getCategoryId().split(",");
            // 保存商品与分类的关系
            InsertGoodCategory(sysGood, split);
        }
    }

    @Override
    public void editGood(SysGood sysGood) {
        int i = this.baseMapper.updateById(sysGood);
        if (i > 0) {
            // 根据角色ID字符串，分割出角色ID数组
            String[] split = sysGood.getCategoryId().split(",");
            // 构造查询Wrapper，用于删除用户原有角色关系
            QueryWrapper<SysCategoryGood> query = new QueryWrapper<>();
            query.lambda().eq(SysCategoryGood::getGoodId,sysGood.getGoodId());
            // 删除用户与角色的原有关系
            sysCategoryGoodService.remove(query);
            // 建立用户与角色的新关系
            InsertGoodCategory(sysGood, split);
        }
    }

    @Override
    public void deleteGood(Long goodId) {
        int i = this.baseMapper.deleteById(goodId);
        // 当删除的行数大于0，即用户存在时，删除该用户所有关联的角色信息
        if (i > 0) {
            QueryWrapper<SysCategoryGood> query = new QueryWrapper<>();
            // 构造查询条件，根据用户ID删除用户角色关联信息
            query.lambda().eq(SysCategoryGood::getGoodId, goodId);
            sysCategoryGoodService.remove(query);
        }
    }

    //
    private void InsertGoodCategory(SysGood sysGood, String[] split) {
        if (split.length > 0) {
            // 初始化一个空的分类列表，用于存储商品与分类关系数据。
            List<SysCategoryGood> categorys = new ArrayList<>();

            for (String s : split) {
                // 为每个商品ID创建一个商品，分类关系对象，并设置商品id，分类id。
                SysCategoryGood categoryGood = new SysCategoryGood();
                categoryGood.setGoodId(sysGood.getGoodId());
                categoryGood.setCategoryId(Long.parseLong(s));
                categorys.add(categoryGood); // 将商品，分类关系，存储到商品与分类列表中。
            }
            // 批量保存用户角色关系到数据库。
            sysCategoryGoodService.saveBatch(categorys);
        }
    }
    //根据前端传递分类id，以及min，max查询对应goodId信息
    @Override
    public List<SysGood> getConditionGoodId(GoodCondition goodCondition) {
        //set去重查询 关系表 id goodId categoryId
        Set<Long> set = this.baseMapper.selectList(null).stream()
                .map(SysGood::getGoodId)
                .collect(Collectors.toSet());
        //判断传递的分类id是否为空
        if(StringUtils.isNotBlank(goodCondition.getCategoryIds())){
            HashSet<Long> set1 = new HashSet<>();
            String[] split = goodCondition.getCategoryIds().split(",");
            List<String> categoryList = Arrays.asList(split);
            // 使用HashSet来存储分类ID，以提高查找效率
            Set<String> categorySet = new HashSet<>(categoryList);
            // 使用增强for循环遍历商品，使用HashSet查找分类ID是否包含
            sysCategoryGoodService.list().forEach(item -> {
                if (categorySet.contains(item.getCategoryId().toString())) {
                    set1.add(item.getGoodId());
                }
            });
            if(!set1.isEmpty()){
                //取交集
                set.retainAll(set1);
            }
        }
        if(StringUtils.isNotBlank(goodCondition.getPriceMin())){
            QueryWrapper<SysGood> query = new QueryWrapper<>();
            query.lambda().ge(SysGood::getPrice,goodCondition.getPriceMin());
            //所有大于min元素
            Set<Long> set2 = this.baseMapper.selectList(query).stream().map(SysGood::getGoodId).collect(Collectors.toSet());
            if(!set2.isEmpty()){
                //取交集
                set.retainAll(set2);
            }
        }
        if(StringUtils.isNotBlank(goodCondition.getPriceMax())){
            QueryWrapper<SysGood> query = new QueryWrapper<>();
            query.lambda().le(SysGood::getPrice,goodCondition.getPriceMax());
            //所有小于max元素
            Set<Long> set3 = this.baseMapper.selectList(query).stream().map(SysGood::getGoodId).collect(Collectors.toSet());
            if(!set3.isEmpty()){
                //取交集
                set.retainAll(set3);
            }
        }
        //获取set中存储goodId对应的商品信息
        return this.baseMapper.selectBatchIds(set);
    }

}
