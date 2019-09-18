package com.example.springsecurity.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * FileName: ValidateCodeException
 * Author:   haichaoyang3
 * Date:     2019/9/18 14:43
 * Description:
 * History:
 * since: 1.0.0
 */
public class ValidateCodeException extends AuthenticationException {


    public ValidateCodeException(String message) {
        super(message);
//        this.code = code;
    }
}
