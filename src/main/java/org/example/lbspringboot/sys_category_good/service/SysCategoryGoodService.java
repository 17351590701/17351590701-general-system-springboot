package org.example.lbspringboot.sys_category_good.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.lbspringboot.sys_category_good.entity.SaveCategoryParam;
import org.example.lbspringboot.sys_category_good.entity.SysCategoryGood;

import java.util.HashMap;

/**
 * @author zyr
 * @date 2024/4/22 下午9:43
 * @Description
 */
public interface  SysCategoryGoodService extends IService<SysCategoryGood> {
    void saveCategory(SaveCategoryParam param);
    HashMap<String,Long> getValueCategory();
}
