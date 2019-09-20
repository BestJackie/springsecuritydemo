package com.example.springsecurity.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * FileName: SysUser
 * Author:   haichaoyang3
 * Date:     2019/9/18 11:40
 * Description:
 * History:
 * since: 1.0.0
 */
@Data
@AllArgsConstructor
public class SysUser {
    private long id;
    private String username;
    private String password;
    private List<SysPermission> sysPermissions;
}
