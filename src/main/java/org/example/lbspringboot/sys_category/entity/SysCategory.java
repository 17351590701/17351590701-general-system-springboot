package org.example.lbspringboot.sys_category.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author zyr
 * @date 2024/4/22 上午8:09
 * @Description
 */
@Data
@TableName("sys_category")
public class SysCategory {
    @TableId(type= IdType.AUTO)
    private Long categoryId;
    private String categoryName;
    private String remark;
    private Date createTime;
    private Date updateTime;

}
