package com.example.springsecurity.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * FileName: ImageCode
 * Author:   haichaoyang3
 * Date:     2019/9/18 13:58
 * Description:验证码
 * History:
 * since: 1.0.0
 */
@Data
@AllArgsConstructor
public class ImageCode {
    private BufferedImage image;
    private String code;
    private LocalDateTime expireTime;

    public ImageCode(BufferedImage image, String code, int expireTime) {
        this.image = image;
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireTime);
    }

    public boolean isExpried(){
        return LocalDateTime.now().isAfter(expireTime);
    }
}
