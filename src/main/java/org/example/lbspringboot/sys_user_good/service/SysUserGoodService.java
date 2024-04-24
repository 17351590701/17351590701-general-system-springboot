package org.example.lbspringboot.sys_user_good.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.lbspringboot.sys_good.entity.SysGood;
import org.example.lbspringboot.sys_user.entity.ShopParam;
import org.example.lbspringboot.sys_user_good.entity.SysUserGood;

import java.util.List;

/**
 * @author zyr
 * @date 2024/4/24 下午9:29
 * @Description
 */
public interface SysUserGoodService extends IService<SysUserGood> {
    IPage<SysGood> userGoodList(ShopParam param);
}
