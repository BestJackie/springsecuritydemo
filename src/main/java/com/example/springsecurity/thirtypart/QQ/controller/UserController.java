package com.example.springsecurity.thirtypart.QQ.controller;

import com.example.springsecurity.demo.model.SysUser;
import com.example.springsecurity.thirtypart.QQ.model.SocialUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * FileName: UserController
 * Author:   haichaoyang3
 * Date:     2019/9/19 14:29
 * Description:
 * History:
 * since: 1.0.0
 */
@RestController
public class UserController {
    @Autowired(required = false)
    private ProviderSignInUtils providerSignInUtils;

    @GetMapping("/user/me")
    public String me(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.toString();
    }

    @GetMapping("/social/user")
    public SocialUserInfo getSocialUserInfo(HttpServletRequest request){
        SocialUserInfo userInfo = new SocialUserInfo();
        Connection<?> connection = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
        userInfo.setProviderId(connection.getKey().getProviderId());
        userInfo.setNickname(connection.getDisplayName());
        userInfo.setHeadimg(connection.getImageUrl());
        return userInfo;
    }

    @PostMapping("/user/regist")
    public void regist(SysUser user,HttpServletRequest request){
        String userId = user.getUsername();
        providerSignInUtils.doPostSignUp(userId,new ServletWebRequest(request));
    }



}
