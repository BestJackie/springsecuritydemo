package com.example.springsecurity.thirtypart.QQ.conn;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

/**
 * FileName: MyConnectionSignUp
 * Author:   haichaoyang3
 * Date:     2019/9/19 11:05
 * Description:
 * History:
 * since: 1.0.0
 */
@Component
public class MyConnectionSignUp implements ConnectionSignUp {

    @Override
    public String execute(Connection<?> connection) {
        // 根据社交用于信息默认创建用户并返回用户的唯一标识
        return connection.getDisplayName();
    }
}
