package com.example.springsecurity.thirtypart.QQ.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * FileName: QQProperties
 * Author:   haichaoyang3
 * Date:     2019/9/19 10:58
 * Description:
 * History:
 * since: 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "social.qq")
public class QQProperties {

    private String filterProcessesUrl = "/auth";
    private String providerId = "qq";
    private String appId;
    private String appSecret;
    private String singnupUrl;

}
