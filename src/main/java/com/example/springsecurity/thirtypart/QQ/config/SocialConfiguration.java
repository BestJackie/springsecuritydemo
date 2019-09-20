package com.example.springsecurity.thirtypart.QQ.config;

import com.example.springsecurity.thirtypart.QQ.conn.MyConnectionSignUp;
import com.example.springsecurity.thirtypart.QQ.model.QQProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.AuthenticationNameUserIdSource;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * FileName: SocialConfiguration
 * Author:   haichaoyang3
 * Date:     2019/9/19 11:09
 * Description:
 * History:
 * since: 1.0.0
 */
@Configuration
@EnableSocial
public class SocialConfiguration extends SocialConfigurerAdapter {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private QQProperties properties;
    @Autowired
    private MyConnectionSignUp myConnectionSignUp;

    /**
     * 建表 spring-social-core-1.1.4.RELEASE.jar/org/springframework/social/connect/jdbc/JdbcUsersConnectionRepository.sql
     *
     * create table UserConnection (userId varchar(255) not null,
     * 	providerId varchar(255) not null,
     * 	providerUserId varchar(255),
     * 	rank int not null,
     * 	displayName varchar(255),
     * 	profileUrl varchar(512),
     * 	imageUrl varchar(512),
     * 	accessToken varchar(512) not null,
     * 	secret varchar(512),
     * 	refreshToken varchar(512),
     * 	expireTime bigint,
     * 	primary key (userId, providerId, providerUserId));
     * create unique index UserConnectionRank on UserConnection(userId, providerId, rank);
     * @param connectionFactoryLocator
     * @return
     */
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {

        JdbcUsersConnectionRepository jdbcUsersConnectionRepository = new JdbcUsersConnectionRepository(dataSource,connectionFactoryLocator, Encryptors.noOpText());

        jdbcUsersConnectionRepository.setTablePrefix("");
        if (Objects.nonNull(myConnectionSignUp))
            jdbcUsersConnectionRepository.setConnectionSignUp(myConnectionSignUp);
        return jdbcUsersConnectionRepository;
    }

    @Bean
    public MySpringSocialConfigurer mySpringSocialConfigurer(){
        MySpringSocialConfigurer springSocialConfigurer = new MySpringSocialConfigurer(properties.getFilterProcessesUrl());
        springSocialConfigurer.userIdSource(getUserIdSource());
        springSocialConfigurer.signupUrl(properties.getSingnupUrl());
        return springSocialConfigurer;
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator){
        return new ProviderSignInUtils(connectionFactoryLocator,getUsersConnectionRepository(connectionFactoryLocator));
    }
}
