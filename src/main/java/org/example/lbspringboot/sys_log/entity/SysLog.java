package org.example.lbspringboot.sys_log.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zyr
 * @date 2024/4/25 上午8:33
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_log")
public class SysLog {
    @TableId(type= IdType.AUTO)
    private Long id;
    private String info;

    public SysLog(String info){
        this.info=info;
    }
}
