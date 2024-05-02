// /*全局异常*/
// package org.example.shopping.exception;
//
// import lombok.extern.slf4j.Slf4j;
// import org.example.shopping.utils.Result;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.RestControllerAdvice;
//
// @RestControllerAdvice(basePackages="com.example.Controller")
// @Slf4j
// public class GlobalExceptionHandler {
//
//     //统一异常处理@ExceptionHandler,主要用于Exception
//     @ExceptionHandler(Exception.class)
//     public Result error(Exception e){
//         log.error("异常信息：",e);
//         return Result.error("系统异常");
//     }
// /* 自定义异常*/
//     @ExceptionHandler(CustomException.class)
//     public Result customError(CustomException e){
//         return Result.error(e.getMsg());
//     }
// }