package com.example.springsecurity.demo.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * FileName: MyAuthenticationFailureHandler
 * Author:   haichaoyang3
 * Date:     2019/9/18 14:36
 * Description:认证失败处理器
 * History:
 * since: 1.0.0
 */
@Slf4j
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("认证失败");
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(exception.getMessage());
    }
}
