package org.example.shopping.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.shopping.sys_good.entity.SysGood;
import org.example.shopping.sys_good.service.SysGoodService;
import org.example.shopping.sys_log.entity.SysLog;
import org.example.shopping.sys_log.service.SysLogService;
import org.example.shopping.sys_user.entity.CancelParam;
import org.example.shopping.sys_user.entity.SysUser;
import org.example.shopping.sys_user.service.SysUserService;
import org.example.shopping.sys_user_good.entity.SysUserGood;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author zyr
 * @date 2024/4/25 上午8:11
 * @Description
 */
@Slf4j
@Aspect
@Component
public class UserAop {
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysGoodService sysGoodService;
    @Resource
    private SysLogService sysLogService;
    //加入购物车
    @Pointcut(value="execution(* org..sys_user.controller.SysUserController.shopCart(..))")
    public void join(){}

    //取消购物车
    @Pointcut(value="execution(* org..sys_user.controller.SysUserController.Cancel(..))")
    public void cancel(){}


    @Around(value="join()")
    public Object joinAround(ProceedingJoinPoint pjp) throws Throwable {
        Object res = pjp.proceed();
        Object[] args = pjp.getArgs();
        for (Object arg : args) {
            if(arg instanceof SysUserGood sysUserGood){
                QueryWrapper<SysUser> queryUser = new QueryWrapper<>();
                queryUser.lambda().eq(SysUser::getUserId,sysUserGood.getUserId());
                String username = sysUserService.getOne(queryUser).getUsername();
                System.out.println(username);
                QueryWrapper<SysGood> queryGood = new QueryWrapper<>();
                queryGood.lambda().eq(SysGood::getGoodId,sysUserGood.getGoodId());
                String goodName = sysGoodService.getOne(queryGood).getGoodName();
                System.out.println(goodName);
                String info = "用户Id: "+sysUserGood.getUserId()+",用户名称:" + username + " ,购买了商品Id: " + sysUserGood.getGoodId() + ",商品名称: " + goodName;
                //记录日志到数据库
                logInfo(info);
            }else{
                System.out.println("无法识别的参数类型: " + arg.getClass().getName());
            }
        }
        return res;
    }

    @Around(value="cancel()")
    public Object cancelAround(ProceedingJoinPoint pjp) throws Throwable {
        Object res = pjp.proceed();
        Object[] args = pjp.getArgs();
        for (Object arg : args) {
            if(arg instanceof CancelParam cancelParam){
                QueryWrapper<SysUser> queryUser = new QueryWrapper<>();
                queryUser.lambda().eq(SysUser::getUserId,cancelParam.getUserId());
                String username = sysUserService.getOne(queryUser).getUsername();
                System.out.println(username);
                QueryWrapper<SysGood> queryGood = new QueryWrapper<>();
                queryGood.lambda().eq(SysGood::getGoodId,cancelParam.getGoodId());
                String goodName = sysGoodService.getOne(queryGood).getGoodName();
                System.out.println(goodName);
                String info = "用户Id: "+cancelParam.getUserId() +",用户名称:" +username + " ,退单商品Id: " + cancelParam.getGoodId() + ",商品名称: " + goodName;
                //记录日志到数据库
                logInfo(info);

            }else{
                System.out.println("无法识别的参数类型: " + arg.getClass().getName());
            }
        }
        return res;
    }
    //记录用户商品交易至数据库
    public void logInfo(String info){
        boolean save = sysLogService.save(new SysLog(info,new Date()));
    }


}
