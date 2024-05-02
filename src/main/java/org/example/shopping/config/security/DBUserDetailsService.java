package org.example.shopping.config.security;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.shopping.config.security.exception.CustomerAuthenionException;
import org.example.shopping.sys_menu.entity.SysMenu;
import org.example.shopping.sys_menu.service.SysMenuService;
import org.example.shopping.sys_user.entity.SysUser;
import org.example.shopping.sys_user.service.SysUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author zyr
 * @date 2024/5/2 上午11:49
 * @Description security 从数据库获取用户
 */
@Slf4j
@Component
public class DBUserDetailsService implements UserDetailsService {
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysMenuService sysMenuService;

    // 重写使security知道用户从哪里来
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = sysUserService.loadUser(username);
        if (Objects.isNull(user)) {
            throw new CustomerAuthenionException("用户名错误或账户不存在!");
        }
        // 查询菜单权限
        List<SysMenu> menuList = null;
        // 判断是否为超级管理员
        if (StringUtils.isNotEmpty(user.getIsAdmin()) && "1".equals((user.getIsAdmin()))) {
            // 是就全部查询
            menuList = sysMenuService.list();
        } else {
            menuList = sysMenuService.getMenuByUserId(user.getUserId());
        }

        List<String> list = Optional.ofNullable(menuList)
                .orElse(new ArrayList<>())
                .stream()
                .filter(item -> item != null && StringUtils.isNotEmpty(item.getCode()))
                .map(SysMenu::getCode)
                .toList();
        //将权限字段转为数组并存入权限Authorities中管理
        String[] array = list.toArray(new String[list.size()]);
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(array);
        user.setAuthorities(authorityList);
        // SysUser 已经继承了UserDetails接口
        return user;
    }
}
