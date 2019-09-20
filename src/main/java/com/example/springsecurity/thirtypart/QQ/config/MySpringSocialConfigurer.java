package com.example.springsecurity.thirtypart.QQ.config;

import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * FileName: MySpringSocialConfigurer
 * Author:   haichaoyang3
 * Date:     2019/9/19 11:06
 * Description:
 * History:
 * since: 1.0.0
 */
public class MySpringSocialConfigurer extends SpringSocialConfigurer {
    private String filterProcessesUrl;

    public MySpringSocialConfigurer(String filterProcessesUrl) {
        this.filterProcessesUrl = filterProcessesUrl;
    }

    @Override
    protected <T> T postProcess(T object) {
        SocialAuthenticationFilter filter = (SocialAuthenticationFilter)super.postProcess(object);
        filter.setFilterProcessesUrl(filterProcessesUrl);
        return (T)filter;
    }
}
