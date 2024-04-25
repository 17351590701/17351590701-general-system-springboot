package org.example.shopping.sys_user_good.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author zyr
 * @date 2024/4/24 下午9:27
 * @Description
 */
@Data
@TableName("sys_user_good")
public class SysUserGood {
    @TableId(type= IdType.AUTO)
    private Long id;
    private Long userId;
    private Long goodId;
}
