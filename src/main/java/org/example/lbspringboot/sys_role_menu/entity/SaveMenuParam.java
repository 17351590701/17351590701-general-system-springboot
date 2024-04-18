package org.example.lbspringboot.sys_role_menu.entity;

import lombok.Data;

import java.util.List;

/**
 * @author zyr
 * @date 2024/4/18 上午10:26
 * @Description
 */
@Data
public class SaveMenuParam {
    private Long roleId;
    private List<Long> list;
}
