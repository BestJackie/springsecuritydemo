package com.example.springsecurity.thirtypart.weixin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.springsecurity.thirtypart.weixin.model.WeixinUserInfo;
import com.example.springsecurity.thirtypart.weixin.service.Weixin;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.nio.charset.Charset;
import java.util.List;

/**
 * FileName: WeixinImpl
 * Author:   haichaoyang3
 * Date:     2019/9/20 10:49
 * Description:
 * History:
 * since: 1.0.0
 */
public class WeixinImpl extends AbstractOAuth2ApiBinding implements Weixin {


    public WeixinImpl(String accessToken) {
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
    }

    private static final String URL_GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?openid=";

    @Override
    protected List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
        messageConverters.remove(0);
        messageConverters.add(new StringHttpMessageConverter(Charset.defaultCharset()));
        return messageConverters;
    }

    @Override
    public WeixinUserInfo getUserInfo(String openId) {
        String url = URL_GET_USER_INFO+openId;
        String response = getRestTemplate().getForObject(url,String.class);
        if (response.contains("errcode"))
            return null;
        WeixinUserInfo userInfo = JSONObject.parseObject(response,WeixinUserInfo.class);
        return userInfo;
    }
}
