package com.example.springsecurity.thirtypart.bind.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;

/**
 * FileName: SocialConfiguration
 * Author:   haichaoyang3
 * Date:     2019/9/23 10:50
 * Description:
 * History:
 * since: 1.0.0
 */
@Configuration
@EnableSocial
public class SocialConfiguration extends SocialConfigurerAdapter {

    @Bean
    public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository){
        return new ConnectController(connectionFactoryLocator,connectionRepository);
    }

}
