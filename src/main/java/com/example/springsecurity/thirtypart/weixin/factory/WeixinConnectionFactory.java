package com.example.springsecurity.thirtypart.weixin.factory;

import com.example.springsecurity.thirtypart.weixin.adapter.WeixinAdapter;
import com.example.springsecurity.thirtypart.weixin.model.WeixinAccessGrant;
import com.example.springsecurity.thirtypart.weixin.service.Weixin;
import com.example.springsecurity.thirtypart.weixin.service.provider.WeixinServiceProvider;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

/**
 * FileName: WeixinConnectionFactory
 * Author:   haichaoyang3
 * Date:     2019/9/20 11:26
 * Description:
 * History:
 * since: 1.0.0
 */
public class WeixinConnectionFactory extends OAuth2ConnectionFactory<Weixin> {

    public WeixinConnectionFactory(String providerId, String appId,String appSecret) {
        super(providerId, new WeixinServiceProvider(appId,appSecret), new WeixinAdapter());
    }

    @Override
    protected String extractProviderUserId(AccessGrant accessGrant) {
        if (accessGrant instanceof WeixinAccessGrant){
            return ((WeixinAccessGrant) accessGrant).getOpenId();
        }
        return null;
    }

    @Override
    public Connection<Weixin> createConnection(AccessGrant accessGrant) {

        return new OAuth2Connection<Weixin>(getProviderId(), extractProviderUserId(accessGrant), accessGrant.getAccessToken(),
                accessGrant.getRefreshToken(), accessGrant.getExpireTime(), getOAuth2ServiceProvider(), getApiAdapter());
    }


    public OAuth2ServiceProvider<Weixin> getOAuth2ServiceProvider() {
        return (OAuth2ServiceProvider<Weixin>) getServiceProvider();
    }

    protected ApiAdapter<Weixin> getApiAdapter(String providerUserId) {
        return new WeixinAdapter(providerUserId);
    }
}
