package com.savelms.api.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savelms.api.auth.controller.dto.LoginResponseDto;
import com.savelms.core.user.domain.entity.User;
import com.savelms.core.user.role.domain.entity.UserRole;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

// spring security 필터를 스프링 필터체인에 동록
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)//Secured, PrePost 어노테이션 활성화
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().configurationSource(corsConfigurationSource());
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        http.authorizeRequests(
                authorize -> authorize
                    .antMatchers(HttpMethod.POST, "/api/users").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/auth/logout").authenticated()
                    .antMatchers(HttpMethod.POST, "/api/auth/password-inquery").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/auth/email*").permitAll()

                    .antMatchers("/api-docs/**").permitAll()
                    .antMatchers("/**").authenticated()
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
                        LoginResponseDto body = new LoginResponseDto();
                        res.setStatus(200);
                        res.setContentType("application/json");
                        res.setCharacterEncoding("utf-8");
//                        addSameSiteCookieAttribute(res);
                        body.setId(user.getApiId());
                        UserRole userRole = user.getUserRoles()
                            .stream()
                            .filter(ur ->
                                ur.getCurrentlyUsed())
                            .findFirst().get();
                        body.setRole(userRole.getRole().getValue());
                        res.getWriter().write(objectMapper.writeValueAsString(body));
                    })
                    .failureHandler((req, res, e) -> {
                        res.setStatus(401);
                    })
                    .usernameParameter("username")
                    .loginProcessingUrl("/api/auth/login")
                    .permitAll()
            )
            .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/api/auth/logout"))
            .logoutSuccessHandler((request, response, authentication) -> {
                response.setStatus(HttpServletResponse.SC_OK);
            })
            .deleteCookies("SESSION")
            .invalidateHttpSession(true);

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://www.save9cadet.com", "http://localhost:3000"));
        configuration.setAllowedMethods(List.of("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**");

    }


    //인증 방식 수동 지정. userDetailsService, passwordEncoder 하나일때는 상관없음.
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(new JpaUserDetailService(userRepository)).passwordEncoder(bCryptPasswordEncoder());
//    }

//    private void addSameSiteCookieAttribute(HttpServletResponse response) {
//        Collection<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);
//        boolean firstHeader = true;
//        // there can be multiple Set-Cookie attributes
//        for (String header : headers) {
//            if (firstHeader) {
//                response.setHeader(HttpHeaders.SET_COOKIE,
//                    String.format("%s; %s", header, "SameSite=None"));
//                firstHeader = false;
//                continue;
//            }
//            response.addHeader(HttpHeaders.SET_COOKIE,
//                String.format("%s; %s", header, "SameSite=None"));
//        }
//    }

}
