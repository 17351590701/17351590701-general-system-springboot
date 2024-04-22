package org.example.lbspringboot.sys_category_good.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author zyr
 * @date 2024/4/22 下午9:41
 * @Description
 */
@Data
@TableName("sys_category_good")
public class SysCategoryGood {
    @TableId(type= IdType.AUTO)
    private Long categoryGoodId;
    private Long categoryId;
    private Long goodId;
}
