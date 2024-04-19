package org.example.lbspringboot.sys_menu.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zyr
 * @date 2024/4/19 上午10:13
 * @Description  动态路由菜单生成
 */
@Data
//null，集合数组等没有内容，空字符串等，都不会被序列化
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVo {
    private String path;
    private String component;
    private String name;
    private String redirect;
    private Meta meta;

    @Data
    @AllArgsConstructor
    public  class Meta {
        private String title;
        private String icon;
        private Object[] roles;
    }

    private List<RouterVo> children = new ArrayList<>();
}
