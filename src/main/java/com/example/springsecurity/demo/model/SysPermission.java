package com.example.springsecurity.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * FileName: SysPermission
 * Author:   haichaoyang3
 * Date:     2019/9/18 11:37
 * Description:
 * History:
 * since: 1.0.0
 */
@Data
@AllArgsConstructor
public class SysPermission implements GrantedAuthority {

    private Long id;
    private String name;
    private String code;
    private String url;
    private String method;
    /**
     * 次方法很重要，用于唯一标识一个权限
     * @return
     */
    @Override
    public String getAuthority() {
        return "ROLE_"+this.code+":"+this.method.toUpperCase();
    }
}
