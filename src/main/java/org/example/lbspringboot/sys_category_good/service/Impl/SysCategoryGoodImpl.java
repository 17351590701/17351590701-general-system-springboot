package org.example.lbspringboot.sys_category_good.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.lbspringboot.sys_category_good.entity.SaveCategoryParam;
import org.example.lbspringboot.sys_category_good.mapper.SysCategoryGoodMapper;
import org.example.lbspringboot.sys_category_good.entity.SysCategoryGood;
import org.example.lbspringboot.sys_category_good.service.SysCategoryGoodService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zyr
 * @date 2024/4/22 下午9:43
 * @Description 保存商品的多重类型
 */
@Service
public class SysCategoryGoodImpl extends ServiceImpl<SysCategoryGoodMapper, SysCategoryGood> implements SysCategoryGoodService {

    @Override
    @Transactional
    public void saveCategory(SaveCategoryParam param) {
        //先删除
        QueryWrapper<SysCategoryGood> query = new QueryWrapper<>();
        query.lambda().eq(SysCategoryGood::getGoodId,param.getGoodId());
        this.baseMapper.delete(query);
        //再保存
        this.baseMapper.saveCategoryGood(param.getGoodId(),param.getList());
    }
}
