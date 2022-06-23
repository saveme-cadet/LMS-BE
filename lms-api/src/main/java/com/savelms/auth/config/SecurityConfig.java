package com.savelms.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
// spring security 필터를 스프링 필터체인에 동록
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)//secured 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests(
            authorize -> authorize
                .antMatchers(HttpMethod.POST, "/api/v2/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/users").permitAll()
                //.mvcMatchers(HttpMethod.GET, "/").hasRole("USER")
                //.mvcMatchers(HttpMethod.GET, "/api/userinfos").hasAnyRole("USER", "MEMBER")

            )
//            .antMatchers("/user/**").authenticated()
//            .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
//            .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .usernameParameter("username")//form tag의 key값 default 는 username
            .loginProcessingUrl("/api/v2/login"); //url로 들어올시 security 가 요청을 낚아채서 대신 로그인 처리를 해줌.
    }


}
