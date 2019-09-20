package com.example.springsecurity.thirtypart.weixin.service.provider;

import com.example.springsecurity.thirtypart.weixin.service.Weixin;
import com.example.springsecurity.thirtypart.weixin.service.impl.WeixinImpl;
import com.example.springsecurity.thirtypart.weixin.template.WeixinOAuth2Template;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * FileName: WeixinServiceProvider
 * Author:   haichaoyang3
 * Date:     2019/9/20 11:22
 * Description:
 * History:
 * since: 1.0.0
 */
public class WeixinServiceProvider extends AbstractOAuth2ServiceProvider {

    private static final String URL_AUTHORIZE="https://open.weixin.qq.com/connect/qrconnect";
    private static final String URL_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";



    public WeixinServiceProvider(String appId,String appSecret) {
        super(new WeixinOAuth2Template(appId,appSecret,URL_AUTHORIZE,URL_ACCESS_TOKEN));
    }

    @Override
    public Weixin getApi(String accessToken) {
        return new WeixinImpl(accessToken);
    }
}
