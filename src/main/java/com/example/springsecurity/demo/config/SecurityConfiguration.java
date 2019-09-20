package com.example.springsecurity.demo.config;

import com.example.springsecurity.demo.filter.ImageValidateCodeFilter;
import com.example.springsecurity.demo.filter.SmsValidateCodeFilter;
import com.example.springsecurity.demo.handler.MyAuthenticationFailureHandler;
import com.example.springsecurity.demo.handler.MyAuthenticationSuccessHandler;
import com.example.springsecurity.demo.service.MyUserDetailsService;
import com.example.springsecurity.thirtypart.QQ.config.MySpringSocialConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * FileName: SecurityConfiguration
 * Author:   haichaoyang3
 * Date:     2019/9/18 12:00
 * Description:
 * History:
 * since: 1.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Autowired
    private MySpringSocialConfigurer mySpringSocialConfigurer;

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        ImageValidateCodeFilter imageValidateCodeFilter = new ImageValidateCodeFilter();
        imageValidateCodeFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);

        SmsValidateCodeFilter smsValidateCodeFilter = new SmsValidateCodeFilter();
        smsValidateCodeFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);

        http.csrf().disable()
                .apply(mySpringSocialConfigurer).and()
                .authorizeRequests()
                .antMatchers("/login","/code/image","/code/sms","/signup","/user/regist")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(imageValidateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(smsValidateCodeFilter,UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(myAuthenticationSuccessHandler)
                .failureUrl("/login?error")
                .permitAll()
                .and()
                .rememberMe()
                .userDetailsService(myUserDetailsService)
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(60*60*60*30)
                .and()
                .logout()
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public PersistentTokenRepository persistentTokenRepository(){
    JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
    tokenRepository.setDataSource(dataSource);
    return tokenRepository;
    }
}
