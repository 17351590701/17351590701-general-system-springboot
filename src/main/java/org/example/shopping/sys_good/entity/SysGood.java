package org.example.shopping.sys_good.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author zyr
 * @date 2024/4/22 上午11:28
 * @Description
 */
@Data
@TableName("sys_good")
public class SysGood {
    @TableId(type= IdType.AUTO)
    private Long goodId;
    private String goodName;
    private String Description;
    private String price;
    private String pictureKey;
    //不属于用户表，需要排除
    @TableField(exist = false)
    private String categoryId;
    private Date createTime;
    private Date updateTime;

}
