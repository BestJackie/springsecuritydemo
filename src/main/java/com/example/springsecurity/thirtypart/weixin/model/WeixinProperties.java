package com.example.springsecurity.thirtypart.weixin.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * FileName: WeixinProperties
 * Author:   haichaoyang3
 * Date:     2019/9/20 11:30
 * Description:
 * History:
 * since: 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "social.weixin")
public class WeixinProperties {

    private String providerId="weixin";
    private String appId;
    private String appSecret;
    private String signupUrl;
}
