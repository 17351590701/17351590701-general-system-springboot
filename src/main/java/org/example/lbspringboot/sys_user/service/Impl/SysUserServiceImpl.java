package org.example.lbspringboot.sys_user.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.lbspringboot.sys_user.entity.SysUser;
import org.example.lbspringboot.sys_user.mapper.SysUserMapper;
import org.example.lbspringboot.sys_user.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * @author zyr
 * @date 2024/4/11 下午5:02
 * @Description
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
}
