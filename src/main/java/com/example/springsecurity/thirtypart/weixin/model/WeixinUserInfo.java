package com.example.springsecurity.thirtypart.weixin.model;

import lombok.Data;

/**
 * FileName: WeixinUserInfo
 * Author:   haichaoyang3
 * Date:     2019/9/19 16:58
 * Description:
 * History:
 * since: 1.0.0
 */
@Data
public class WeixinUserInfo {
    /**
     * 普通用户标识，对当前开发者帐号唯一
     */
    private String openId;

    private String nickname;

    private String language;

    private String sex;

    private String province;

    private String city;

    private String country;

    private String headimgurl;

    private String[] privilege;

    private String unionid;

}
