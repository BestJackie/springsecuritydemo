package com.example.springsecurity.filter;

import com.example.springsecurity.controller.SmsValidateCodeController;
import com.example.springsecurity.controller.ValidateCodeController;
import com.example.springsecurity.exception.ValidateCodeException;
import com.example.springsecurity.model.SmsCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * FileName: SmsValidateCodeFilter
 * Author:   haichaoyang3
 * Date:     2019/9/18 16:39
 * Description:
 * History:
 * since: 1.0.0
 */
public class SmsValidateCodeFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if ("/login".equals(httpServletRequest.getRequestURI())&& "POST".equals(httpServletRequest.getMethod())){
            try {
                validate(new ServletWebRequest(httpServletRequest));
            }catch (ValidateCodeException e){
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    private void validate(ServletWebRequest request) throws ServletRequestBindingException {
        SmsCode codeInSession = (SmsCode) sessionStrategy.getAttribute(request, SmsValidateCodeController.SESSION_KEY);
        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),"smsCode");

        if (StringUtils.isEmpty(codeInRequest))
            throw new ValidateCodeException("验证码不能为空");
        if (Objects.isNull(codeInSession))
            throw new ValidateCodeException("验证码不存在");
        if (codeInSession.isExpried()){
            sessionStrategy.removeAttribute(request,ValidateCodeController.SESSION_KEY);
            throw new ValidateCodeException("验证码已过期");
        }
        if (!codeInRequest.equals(codeInSession.getCode()))
            throw new ValidateCodeException("验证码不匹配");
        sessionStrategy.removeAttribute(request,ValidateCodeController.SESSION_KEY);

    }
    public AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return authenticationFailureHandler;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }
}
