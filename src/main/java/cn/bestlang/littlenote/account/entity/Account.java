package cn.bestlang.littlenote.account.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author gen
 * @since 2021-03-12
 */
@Data
public class Account {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    /**
     * 账户类型
     */
    private String type;

    /**
     * 账户名
     */
    private String name;

    private String password;

    /**
     * 备注
     */
    private String notes;

    /**
     * 图标
     */
    private String icon;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
