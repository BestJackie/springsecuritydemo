package com.example.springsecurity.thirtypart.QQ.conn;

import com.example.springsecurity.thirtypart.QQ.adapter.QQAdapter;
import com.example.springsecurity.thirtypart.QQ.service.QQ;
import com.example.springsecurity.thirtypart.QQ.service.provider.QQServiceProvider;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * FileName: QQConnectionFactory
 * Author:   haichaoyang3
 * Date:     2019/9/19 10:54
 * Description:
 * History:
 * since: 1.0.0
 */
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ> {
    public QQConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new QQServiceProvider(appId, appSecret), new QQAdapter());
    }
}
