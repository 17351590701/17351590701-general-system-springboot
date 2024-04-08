package org.example.lbspringboot.sys_role.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author zyr
 * @date 2024/4/8 下午10:07
 * @Description
 */
@Data
@TableName("sys_role")
public class SysRole {
    @TableId(type = IdType.AUTO)
    public Long roleId;
    private String roleName;
    private String type;
    private Date createTime;
    private Date updateTime;

}
