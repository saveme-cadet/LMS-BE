package com.savelms.api.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savelms.api.auth.controller.dto.LoginResponseDto;
import com.savelms.core.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// spring security 필터를 스프링 필터체인에 동록
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)//Secured, PrePost 어노테이션 활성화
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests(
                authorize -> authorize
                    //.antMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/users").permitAll()
                    .antMatchers(HttpMethod.GET, "/swagger-ui/index.html**").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/auth/email** ").permitAll()

                //.mvcMatchers(HttpMethod.GET, "/").hasRole("USER")
                //.mvcMatchers(HttpMethod.GET, "/api/userinfos").hasAnyRole("USER", "MEMBER")
            )
//            .antMatchers("/user/**").authenticated()
//            .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
//            .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .formLogin(loginConfigurer ->
                loginConfigurer
                    .successHandler((req, res, auth) -> {
                        User user = (User)auth.getPrincipal();
                        LoginResponseDto response = new LoginResponseDto();
                        res.setStatus(200);
                        res.setContentType("application/json");
                        res.setCharacterEncoding("utf-8");
                        response.setId(user.getApiId());

                        res.getWriter().write(objectMapper.writeValueAsString(response));
                    })
                    .failureHandler((req, res, e) -> {
                        res.setStatus(401);
                    })
                    .usernameParameter("username")
                    .loginProcessingUrl("/api/auth/login")
                    .permitAll()
            )
            .logout().logoutRequestMatcher(new AntPathRequestMatcher("/api/auth/logout"))
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true);

    }

    //인증 방식 수동 지정. userDetailsService, passwordEncoder 하나일때는 상관없음.
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(new JpaUserDetailService(userRepository)).passwordEncoder(bCryptPasswordEncoder());
//    }

}
