package org.example.shopping.sys_category_good.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.shopping.sys_category.service.SysCategoryService;
import org.example.shopping.sys_category_good.entity.SaveCategoryParam;
import org.example.shopping.sys_category_good.mapper.SysCategoryGoodMapper;
import org.example.shopping.sys_category_good.entity.SysCategoryGood;
import org.example.shopping.sys_category_good.service.SysCategoryGoodService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * @author zyr
 * @date 2024/4/22 下午9:43
 * @Description 保存商品的多重类型
 */
@Service
public class SysCategoryGoodImpl extends ServiceImpl<SysCategoryGoodMapper, SysCategoryGood> implements SysCategoryGoodService {
    @Resource
    private SysCategoryService sysCategoryService;

    @Override
    @Transactional
    public void saveCategory(SaveCategoryParam param) {
        // 先删除
        QueryWrapper<SysCategoryGood> query = new QueryWrapper<>();
        query.lambda().eq(SysCategoryGood::getGoodId, param.getGoodId());
        this.baseMapper.delete(query);
        // 再保存
        this.baseMapper.saveCategoryGood(param.getGoodId(), param.getList());
    }

    @Override
    public HashMap<String, Long> getValueCategory() {
        // 村粗分类id与次数
        HashMap<Long, Long> map = new HashMap<>();
        // 存储categoryId集合
        List<Long> lists = this.baseMapper.selectList(null).stream()
                .map(SysCategoryGood::getCategoryId)
                .toList();
        // 关系表id goodId categoryId
        for (Long list : lists) {
            if (map.containsKey(list)) {
                // 获取当前分类id次数
                map.put(list, map.get(list) + 1);
            } else {
                map.put(list, 1L);
            }
        }

        // 存储分类名与次数
        HashMap<String, Long> hm = new HashMap<>();
        sysCategoryService.list()
                .forEach(item -> { // 先获取分类名和分类id，之后将id替换为次数
                    hm.put(item.getCategoryName(), item.getCategoryId());
                });
        // 遍历分类名
        hm.keySet().forEach(name -> {
            // 遍历分类id
            map.keySet().forEach(id -> {
                // 如果id等于分类码的id
                if (id.equals(hm.get(name))) {
                    // 替换hm中的value为hm中的value
                    hm.put(name, map.get(id));
                }
            });
        });

        return hm;
    }
}
