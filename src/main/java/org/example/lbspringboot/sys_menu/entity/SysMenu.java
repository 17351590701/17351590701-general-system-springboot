package org.example.lbspringboot.sys_menu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zyr
 * @date 2024/4/13 下午8:01
 * @Description
 */
@Data
@TableName("sys_menu")
public class SysMenu {
    private Long menuId;
    private Long parentId;
    private String title;
    private String code;
    private String path;
    private String url;
    private String type;
    private String icon;
    //上级菜单名称
    private String parentName;
    //序号
    private Integer orderNum;
    private Date createTime;
    private Date updateTime;
    // 下级菜单，children字段不属于sys——menu表，需要排除
    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();
    @TableField(exist = false)
    private Long value;
    @TableField(exist = false)
    private String label;

}
