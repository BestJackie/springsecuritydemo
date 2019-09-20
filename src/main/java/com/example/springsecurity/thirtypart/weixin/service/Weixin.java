package com.example.springsecurity.thirtypart.weixin.service;

import com.example.springsecurity.thirtypart.weixin.model.WeixinUserInfo;

/**
 * FileName: Weixin
 * Author:   haichaoyang3
 * Date:     2019/9/19 17:10
 * Description:
 * History:
 * since: 1.0.0
 */
public interface Weixin {
    WeixinUserInfo getUserInfo(String openId);
}
