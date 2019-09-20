package com.example.springsecurity.thirtypart.QQ.config;

import com.example.springsecurity.thirtypart.QQ.conn.QQConnectionFactory;
import com.example.springsecurity.thirtypart.QQ.model.QQProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;

/**
 * FileName: QQAutoConfig
 * Author:   haichaoyang3
 * Date:     2019/9/19 11:21
 * Description:
 * History:
 * since: 1.0.0
 */
@Configuration
@ConditionalOnProperty(prefix = "social.qq" ,name = "appId")
@EnableConfigurationProperties(QQProperties.class)
public class QQAutoConfig extends SocialConfigurerAdapter {
    @Autowired
    private QQProperties qqProperties;

    protected ConnectionFactory<?> createConnectionFactory(){
            return new QQConnectionFactory(qqProperties.getProviderId(),qqProperties.getAppId(),qqProperties.getAppSecret());
    }

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
        connectionFactoryConfigurer.addConnectionFactory(this.createConnectionFactory());
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return null;
    }
}
