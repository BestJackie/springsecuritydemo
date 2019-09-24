package com.example.springsecurity.thirtypart.weixin.config;

import com.example.springsecurity.thirtypart.bind.view.MyConnectedView;
import com.example.springsecurity.thirtypart.weixin.factory.WeixinConnectionFactory;
import com.example.springsecurity.thirtypart.weixin.model.WeixinProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.web.servlet.View;

/**
 * FileName: WeixinAutoConfiguration
 * Author:   haichaoyang3
 * Date:     2019/9/20 11:32
 * Description:
 * History:
 * since: 1.0.0
 */
@Configuration
@ConditionalOnProperty(prefix = "social.weixin", name = "appId")
@EnableConfigurationProperties(WeixinProperties.class)
public class WeixinAutoConfiguration extends SocialConfigurerAdapter {
    @Autowired
    private WeixinProperties weixinProperties;
    protected ConnectionFactory<?>createConnectionFactory(){
        return new WeixinConnectionFactory(weixinProperties.getProviderId(),weixinProperties.getAppId(),weixinProperties.getAppSecret());
    }

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
        connectionFactoryConfigurer.addConnectionFactory(this.createConnectionFactory());
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return null;
    }
    @Bean({"connect/weixin.doConnect", "connect/weixin.doConnected"})
    public View weixinConnectedView() {
        return new MyConnectedView();
    }
}
