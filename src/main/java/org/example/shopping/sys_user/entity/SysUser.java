package org.example.shopping.sys_user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author zyr
 * @date 2024/4/8 下午10:15
 * @Description
 */
@Data
@TableName("sys_user")
public class SysUser {
    @TableId(type = IdType.AUTO)
    private Long userId;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String sex;
    private String isAdmin;
    //不属于用户表，需要排除
    @TableField(exist = false)
    private String roleId;
    //账户过期 1 未过期 0 过期
    private boolean isAccountNonExpired=true;
    //账户锁定 1 未锁定 0 锁定
    private boolean isAccountNonLocked=true;
    //密码枸杞 1 未过期 0 过期
    private boolean isCredentialsNonExpired=true;
    //账户可用 1 可用  0 喊出用户
    private boolean isEnabled=true;
    private String nickName;
    private Date createTime;
    private Date updateTime;
}
