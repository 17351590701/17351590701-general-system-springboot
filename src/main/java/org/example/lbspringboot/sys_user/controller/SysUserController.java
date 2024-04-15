package org.example.lbspringboot.sys_user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;

import javax.imageio.ImageIO;
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
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

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
    private DefaultKaptcha defaultKaptcha;

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

    // 图片验证码
    @PostMapping("/getImage")
    public Result imageCode(HttpServletRequest request) {
        // 获取session
        HttpSession session = request.getSession();
        // 生成验证码
        String text = defaultKaptcha.createText();
        // 存放到session
        session.setAttribute("code", text);
        // 生成图片，转换成base64
        BufferedImage bufferedImage = defaultKaptcha.createImage(text);
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
            // 转换成base64
            byte[] bytes = outputStream.toByteArray();
            String captchaBase64 = Base64.getEncoder().encodeToString(bytes);
            return new Result(200,"生成成功",captchaBase64);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(outputStream!=null){
                    outputStream.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    //登录
    @PostMapping("/login")
    public Result login( HttpServletRequest request,@RequestBody LoginParam param) {
        //获取前端code
        String code=param.getCode();
        //获取session中生成的code
        HttpSession session = request.getSession();
        String imageCode = (String) session.getAttribute("code");
        if(StringUtils.isEmpty(imageCode)){
            return Result.error("验证码过期");
        }
        //判断两个验证码是否相等
        if (imageCode.equals(code)) {
            return Result.error("验证码错误");
        }
        //查询用户信息
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        query.lambda().eq(SysUser::getUsername, param.getUsername());
        SysUser one = sysUserService.getOne(query);
        if(one==null){
            return Result.error("用户不存在");
        }
        //返回用户的信息和token
        LoginVo vo = new LoginVo();
        vo.setUserId(one.getUserId());
        vo.setNickName(one.getNickName());
        return Result.success("登录成功", vo);
    }
}
