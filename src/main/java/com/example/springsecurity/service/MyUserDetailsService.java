package com.example.springsecurity.service;

import com.example.springsecurity.model.SysPermission;
import com.example.springsecurity.model.SysUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

/**
 * FileName: MyUserDetailsService
 * Author:   haichaoyang3
 * Date:     2019/9/18 11:47
 * Description:
 * History:
 * since: 1.0.0
 */
@Service
public class MyUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = new SysUser(1L, "admin", "$2a$10$nm5H9QvnoWao.l7NbxQGZeZoR0Cn.VqCpsl3E/FhglPa954Zg9ccm", Arrays.asList(
                new SysPermission(1L, "用户列表", "user:view", "/getUserList", "GET"),
                new SysPermission(2L, "添加用户", "user:add", "/addUser", "POST"),
                new SysPermission(3L, "修改用户", "user:update", "/updateUser", "PUT")
        ));

        if (Objects.isNull(user))
            throw new UsernameNotFoundException(username);
        return new User(user.getUsername(),user.getPassword(),user.getSysPermissions());
    }

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }
}
