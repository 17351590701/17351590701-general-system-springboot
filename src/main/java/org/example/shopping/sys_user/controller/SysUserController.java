package org.example.shopping.sys_user.controller;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;


import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.shopping.sys_good.entity.SysGood;
import org.example.shopping.sys_menu.entity.AssignTreeParam;
import org.example.shopping.sys_menu.entity.AssignTreeVo;
import org.example.shopping.sys_menu.entity.SysMenu;
import org.example.shopping.sys_menu.service.SysMenuService;
import org.example.shopping.sys_user.entity.*;
import org.example.shopping.sys_user.service.SysUserService;
import org.example.shopping.sys_user_good.entity.SysUserGood;
import org.example.shopping.sys_user_good.service.SysUserGoodService;
import org.example.shopping.sys_user_role.entity.SysUserRole;
import org.example.shopping.sys_user_role.service.SysUserRoleService;
import org.example.shopping.utils.JwtUtils;
import org.example.shopping.utils.Result;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zyr
 * @date 2024/4/11 下午5:04
 * @Description
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/sysUser")
public class SysUserController {
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysUserRoleService sysUserRoleService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private SysMenuService sysMenuService;
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private SysUserGoodService sysUserGoodService;

    // 新增
    @PostMapping
    public Result add(@RequestBody SysUser sysUser) {
        sysUser.setCreateTime(new Date());
        sysUserService.saveUser(sysUser);
        return Result.success("新增成功");
    }

    // 编辑
    @PutMapping
    public Result edit(@RequestBody SysUser sysUser) {
        sysUser.setUpdateTime(new Date());
        sysUserService.editUser(sysUser);
        return Result.success("编辑成功");

    }


    // 删除
    @DeleteMapping("/{userId}")
    public Result delete(@PathVariable("userId") Long userId) {
        sysUserService.deleteUser(userId);
        return Result.success("删除成功");
    }


    /**
     * 获取角色列表
     *
     * @param param 包含分页信息和查询条件的用户参数对象
     * @return 返回用户查询结果，包括查询状态和用户列表
     */
    @GetMapping("/getList")
    public Result getList(SysUserPage param) {
        // 构造分页对象
        IPage<SysUser> page = new Page<>(param.getCurrentPage(), param.getPageSize());
        // 初始化查询条件
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        // 如果parm中的角色名不为空，则将角色名作为查询条件，使用like操作符进行模糊查询
        if (StringUtils.isNotEmpty((param.getNickName()))) {
            query.lambda().like(SysUser::getNickName, param.getNickName());
        }
        // 根据电话查询
        if (StringUtils.isNotEmpty(param.getPhone())) {
            query.lambda().like(SysUser::getPhone, param.getPhone());
        }
        // 根据创建时间进行降序排序
        query.lambda().orderByDesc(SysUser::getCreateTime);
        IPage<SysUser> list = sysUserService.page(page, query);
        return Result.success("查询成功", list);
    }


    // 根据用户id在userRole表中查询用户持有的角色列表。
    @GetMapping("/getRoleList")
    public Result getRoleList(Long userId) {
        QueryWrapper<SysUserRole> query = new QueryWrapper<>();
        query.lambda().eq(SysUserRole::getUserId, userId);
        // 查询，满足条件的userRole表至list中
        List<SysUserRole> list = sysUserRoleService.list(query);
        // 角色id集合
        List<Long> roleList = new ArrayList<>();
        // 判断查询结果是否为空，否则遍历查询结果添加到roleList中
        Optional.ofNullable(list).orElse(new ArrayList<>())
                .forEach(item -> {
                    roleList.add(item.getRoleId());
                });
        return Result.success("查询成功", roleList);
    }

    // 重置密码
    @PostMapping("/resetPassword")
    public Result resetPassword(@RequestBody SysUser sysUser) {
        log.info("重置密码{}", sysUser);
        UpdateWrapper<SysUser> query = new UpdateWrapper<>();
        // 重置密码：666666
        query.lambda().eq(SysUser::getUserId, sysUser.getUserId())
                .set(SysUser::getPassword, "666666");
        if (sysUserService.update(query)) {
            return Result.success("密码重置成功");
        }
        return Result.error("密码重置失败");
    }


    /**
     * 通过GET请求获取一个基于Base64编码的图形验证码。
     *
     * @param response HttpServletResponse对象，用于向客户端发送响应。
     * @return 返回一个表示操作结果的对象，包含成功信息。
     * @throws IOException 如果在输出过程中发生IO异常。
     */
    @GetMapping("/getImage")
    public void imageCodeBase64(HttpServletResponse response) throws IOException {
        log.info("获取了验证码");
        // 设置响应类型为图片格式，将验证码图片输出到浏览器
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "No-cache");

        // 创建一个图形验证码，指定其长度、宽度、字符数和干扰线宽度
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(250, 150, 4, 4);
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", 4);
        captcha.setGenerator(randomGenerator);

        String capText = captcha.getCode();

        // redis设置 60s key 过期
        redisTemplate.opsForValue().set("capText", capText, 60, TimeUnit.SECONDS);
        log.info("验证码：{}", capText);

        captcha.write(response.getOutputStream());
        // response.getOutputStream().close();
    }


    // 登录
    @PostMapping("/login")
    public Result login(@RequestBody LoginParam param) {
        // 获取前端输入的code
        String Ucode = param.getCode();
        // 获取redis中的key
        String capText = (String) redisTemplate.boundValueOps("capText").get();
        if (StringUtils.isEmpty(capText)) {
            return Result.error("验证码过期");
        }
        if (capText.equalsIgnoreCase(Ucode)) {
            // 查询用户信息
            QueryWrapper<SysUser> query = new QueryWrapper<>();
            query.lambda().eq(SysUser::getUsername, param.getUsername());
            SysUser one = sysUserService.getOne(query);
            if (one == null) {
                return Result.error("用户不存在");
            }
            // 返回用户的信息和token
            LoginVo vo = new LoginVo();
            vo.setUserId(one.getUserId());
            vo.setNickName(one.getNickName());
            // 生成token
            Map<String, String> map = new HashMap<>();
            map.put("userId", Long.toString(one.getUserId()));
            String token = jwtUtils.generateToken(map);
            vo.setToken(token);
            return Result.success("登录成功", vo);
        } else {
            return Result.error("验证码错误");
        }
    }


    // 查询根据roleId查询菜单树
    @GetMapping("/getAssingTree")
    public Result getAssingTree(AssignTreeParam param) {
        AssignTreeVo assignTree = sysUserService.getAssignTree(param);
        return Result.success("查询成功", assignTree);
    }

    // 修改密码
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody UpdatePasswordParam param) {
        SysUser user = sysUserService.getById(param.getUserId());
        if (!param.getOldPassword().equals(user.getPassword())) {
            return Result.error("原密码错误");
        }
        UpdateWrapper<SysUser> query = new UpdateWrapper<>();
        query.lambda().set(SysUser::getPassword, param.getNewPassword())
                .eq(SysUser::getUserId, param.getUserId());
        if (sysUserService.update(query)) {
            return Result.success("修改成功");
        } else {
            return Result.error("修改失败");
        }

    }

    // 获取用户信息
    @GetMapping("/getInfo")
    public Result getInfo(Long userId) {
        // 根据id查询用户信息
        SysUser user = sysUserService.getById(userId);
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

        // 设置
        UserInfo userInfo = new UserInfo(user.getUserId(), user.getNickName(), list.toArray());
        return Result.success("查询成功", userInfo);

    }

    // 用户购物存入用户商品关系表
    @PostMapping("/shopCart")
    public Result shopCart(@RequestBody SysUserGood param) {
        sysUserGoodService.save(param);
        return Result.success("购物成功");
    }

    //查询所有购物车信息
    @GetMapping("getShopList")
    public Result getShopList(ShopParam param) {
        IPage<SysGood> sysGoods = sysUserGoodService.userGoodList(param);
        return Result.success("查询成功", sysGoods);
    }

    //取消所选购物订单
    @PostMapping("/cancel")
    public Result Cancel(@RequestBody CancelParam param){
        QueryWrapper<SysUserGood> query = new QueryWrapper<>();
        query.lambda().eq(SysUserGood::getUserId,param.getUserId());
        //查询该userId下的信息表
        sysUserGoodService.list(query)
                .forEach(item->{
                    if(item.getGoodId().equals(param.getGoodId())){
                    sysUserGoodService.removeById(item.getId());
                    }
                });
        return Result.success("取消成功");
    }
}
