package org.example.lbspringboot.sys_category_good.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.lbspringboot.sys_category_good.entity.SysCategoryGood;

import java.util.List;

/**
 * @author zyr
 * @date 2024/4/22 下午9:44
 * @Description
 */
@Mapper
public interface SysCategoryGoodMapper extends BaseMapper<SysCategoryGood> {
    //保存商品类型
    void saveCategoryGood(@Param("goodId")Long goodId, @Param("categoryIds")List<Long> categoryIds);
}
