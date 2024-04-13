package org.example.lbspringboot.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zyr
 * @date 2024/4/8 下午9:33
 * @Description 封装统一返回值类型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private int code;  /*判定成功失败*/
    private String msg;/*消息返回报错信息，前台获取*/
    private T data;    /*接收任何数据*/

    //成功返回 code
    public static Result success() {
        Result result = new Result();
         result.setCode(StatusCode.SUCCESS_CODE);
        return result;
    }
    //成功返回 code,msg
    public static Result success(String msg) {
        Result result = new Result();
        result.setCode(StatusCode.SUCCESS_CODE);
        result.setMsg(msg);
        return result;
    }
    //成功返回 msg data
    public static Result success(String msg, Object data) {
        Result result = success(msg);
        result.setData(data);
        return result;
    }
    //成功返回 code msg data
    public static Result success(String msg, int code, Object data) {
        return Vo(msg, code, data);
    }

    public static Result Vo(String msg, int code, Object data) {
        Result result = error(code, msg);
        result.setData(data);
        return result;
    }

    //失败返回 code
    public static Result error() {
        Result result = new Result();
        result.setCode(StatusCode.ERROR_CODE);
        return result;

    }
    //失败返回 code msg
    public static Result error(String msg) {
        Result result = new Result();
        result.setCode(StatusCode.ERROR_CODE);
        result.setMsg(msg);
        return result;
    }
    //失败返回 code msg
    public static Result error(int code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
    //失败返回 msg data
    public static Result error(String msg, Object data) {
        Result result = error(msg);
        result.setData(data);
        return result;
    }
    //失败返回 code msg data
    public static Result error(String msg, int code, Object data) {
        return Vo(msg, code, data);
    }

}
