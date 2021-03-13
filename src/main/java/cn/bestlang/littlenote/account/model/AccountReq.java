package cn.bestlang.littlenote.account.model;

import lombok.Data;

@Data
public class AccountReq {

    private Integer id;

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
}
