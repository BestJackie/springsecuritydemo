package com.example.springsecurity.thirtypart.QQ.template;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * FileName: QQOAuth2Template
 * Author:   haichaoyang3
 * Date:     2019/9/19 10:36
 * Description:调用api发送请求的模板
 * History:
 * since: 1.0.0
 */
public class QQOAuth2Template extends OAuth2Template {
    public QQOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        setUseParametersForClientAuthentication(true);
    }

    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = super.createRestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.defaultCharset()));
        return restTemplate;
    }

    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        String responseString = getRestTemplate().postForObject(accessTokenUrl,parameters,String.class);
        String[]items = responseString.split("&");
        String accessToken =  items[0].split("=")[1];
        Long exprireIn = new Long(items[1].split("=")[1]);
        String refreshToken = items[2].split("=")[1];
        return new AccessGrant(accessToken,null,refreshToken,exprireIn);
    }
}
