package org.example.lbspringboot.sys_menu.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zyr
 * @date 2024/4/17 下午7:11
 * @Description
 */
@Data
public class AssignTreeVo {
    private Object[] checkList;
    private List<SysMenu> menuList = new ArrayList<>();

}
