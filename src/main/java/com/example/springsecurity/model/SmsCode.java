package com.example.springsecurity.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * FileName: SmsCode
 * Author:   haichaoyang3
 * Date:     2019/9/18 16:30
 * Description:
 * History:
 * since: 1.0.0
 */
@Data
@AllArgsConstructor
public class SmsCode {

    private String code;
    private LocalDateTime expireTime;

    public SmsCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public boolean isExpried(){
        return LocalDateTime.now().isAfter(expireTime);
    }
}
