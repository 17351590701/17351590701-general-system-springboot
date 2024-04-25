package org.example.shopping.sys_user_good.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.shopping.sys_good.entity.SysGood;
import org.example.shopping.sys_good.mapper.SysGoodMapper;
import org.example.shopping.sys_user.entity.ShopParam;
import org.example.shopping.sys_user_good.entity.SysUserGood;
import org.example.shopping.sys_user_good.mapper.SysUserGoodMapper;
import org.example.shopping.sys_user_good.service.SysUserGoodService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zyr
 * @date 2024/4/24 下午9:29
 * @Description
 */
@Service
public class SysUserGoodServiceImpl extends ServiceImpl<SysUserGoodMapper, SysUserGood> implements SysUserGoodService {
    @Resource
    private SysGoodMapper sysGoodMapper;

    // 根据用户id，在用户商品表中查询商品，返回给前端
    @Override
    public IPage<SysGood> userGoodList(ShopParam param) {
        // 参数校验
        if (param.getUserId() == null ||
            param.getCurrentPage() < 1 ||
            param.getPageSize() < 1) {
            throw new IllegalArgumentException("提供无效参数");
        }

        IPage<SysGood> page = new Page<>(param.getCurrentPage(), param.getPageSize());

        try {
            // 在用户关系表中查询该userId的所有goodId
            QueryWrapper<SysUserGood> userGoodQuery = new QueryWrapper<>();
            userGoodQuery.lambda().eq(SysUserGood::getUserId, param.getUserId());
            List<Long> goodIds = this.baseMapper.selectList(userGoodQuery).stream()
                    .map(SysUserGood::getGoodId)
                    .collect(Collectors.toList());

            if (!goodIds.isEmpty()) {
                // 根据goodId查询商品
                QueryWrapper<SysGood> goodQueryWrapper = new QueryWrapper<>();
                goodQueryWrapper.lambda().in(SysGood::getGoodId, goodIds);

                // 返回分页查询结果
                return sysGoodMapper.selectPage(page, goodQueryWrapper);
            } else {
                // 查询不到对应goodId，返回空分页结果
                return new Page<>();
            }
        } catch (Exception e) {
            // 处理异常
            log.error(e.getMessage());
            throw new RuntimeException("加载失败用户商品信息");
        }
    }
}


