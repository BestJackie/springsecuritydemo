package com.example.springsecurity.thirtypart.weixin.model;

import org.springframework.social.oauth2.AccessGrant;

/**
 * FileName: WeixinAccessGrant
 * Author:   haichaoyang3
 * Date:     2019/9/20 11:15
 * Description:
 * History:
 * since: 1.0.0
 */
public class WeixinAccessGrant extends AccessGrant {
    private String openId;

    public WeixinAccessGrant() {
        super("");
    }

    public WeixinAccessGrant(String accessToken, String scope, String refreshToken, Long expiresIn) {
        super(accessToken, scope, refreshToken, expiresIn);
    }

    public String getOpenId() {
        return openId;
    }
}
