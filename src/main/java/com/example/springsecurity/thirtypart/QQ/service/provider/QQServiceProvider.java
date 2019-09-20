package com.example.springsecurity.thirtypart.QQ.service.provider;

import com.example.springsecurity.thirtypart.QQ.service.QQ;
import com.example.springsecurity.thirtypart.QQ.service.impl.QQImpl;
import com.example.springsecurity.thirtypart.QQ.template.QQOAuth2Template;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * FileName: QQServiceProvider
 * Author:   haichaoyang3
 * Date:     2019/9/19 10:47
 * Description:
 * History:
 * since: 1.0.0
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {
    private String appId;
    private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";
    private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";

    public QQServiceProvider(String appId,String appSecret) {
        super(new QQOAuth2Template(appId,appSecret,URL_AUTHORIZE,URL_ACCESS_TOKEN));
        this.appId = appId;
    }

    @Override
    public QQ getApi(String accessToken) {
        return new QQImpl(accessToken,appId);
    }
}
