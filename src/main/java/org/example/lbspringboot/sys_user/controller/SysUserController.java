package org.example.lbspringboot.sys_user.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.google.code.kaptcha.Producer;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.lbspringboot.sys_user.entity.LoginParam;
import org.example.lbspringboot.sys_user.entity.LoginVo;
import org.example.lbspringboot.sys_user.entity.SysUser;
import org.example.lbspringboot.sys_user.entity.SysUserPage;
import org.example.lbspringboot.sys_user.service.SysUserService;
import org.example.lbspringboot.sys_user_role.entity.SysUserRole;
import org.example.lbspringboot.sys_user_role.service.SysUserRoleService;
import org.example.lbspringboot.utils.Result;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * @author zyr
 * @date 2024/4/11 下午5:04
 * @Description
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/sysUser")
public class SysUserController {
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysUserRoleService sysUserRoleService;
    @Resource
    private Producer captchaProducer;

    // 新增
    @PostMapping
    public Result add(@RequestBody SysUser sysUser) {
        log.info("新增用户{}", sysUser);
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
     * 生成并返回图形验证码的Base64编码字符串。
     * 该方法不接受任何参数，返回一个包含验证码文本和其Base64编码图像的结果对象。
     *
     * @return Result 包含验证码文本和其Base64编码图像的结果对象
     * @throws IOException 如果图像写入过程中发生IO异常
     */
    @PostMapping("/getImage")
    public Result imageCodeBase64(HttpServletRequest request) throws IOException {
        // 生成验证码文本
        String capText = captchaProducer.createText();
        log.info("验证码：{}", capText);
        //将验证码文本保存到session中
        HttpSession session = request.getSession();
        session.setAttribute("code",capText);
        // 根据验证码文本生成对应的图像
        BufferedImage bi = captchaProducer.createImage(capText);
        //初始化字节数组输出流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 将图像以JPEG格式写入到字节数组输出流
        ImageIO.write(bi, "jpeg", byteArrayOutputStream);
        // 将字节数组输出流中的图像数据转换为字节数组
        byte[] imageData = byteArrayOutputStream.toByteArray();
        // 将图像数据字节数组编码为Base64字符串
        String base64Image = Base64.getEncoder().encodeToString(imageData);
        // 返回包含验证码文本和Base64编码图像的结果
        return Result.success("获取验证码成功",base64Image);
    }


    // 登录
    @PostMapping("/login")
    public Result login(HttpServletRequest request, @RequestBody LoginParam param) {
        // 获取前端code
        String code = param.getCode();
        // 获取session中生成的code
        HttpSession session = request.getSession();
        String capText = (String) session.getAttribute("code");
        if (StringUtils.isEmpty(capText)) {
            return Result.error("验证码过期");
        }
        // 判断两个验证码是否相等
        if (capText.equals(code)) {
            return Result.error("验证码错误");
        }
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
        return Result.success("登录成功", vo);
    }
}
