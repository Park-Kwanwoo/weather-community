package org.project.weathercommunity.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.weathercommunity.config.security.filter.JwtAuthenticationFilter;
import org.project.weathercommunity.config.security.filter.VueLoginProcessingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
        .and()
                .httpBasic().disable() // Rest API 형태로 개발하기 때문에 비활성 화
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt로 인증하므로 세션 X
        .and()
                .csrf().disable()
                // h2 console 접속을 위해서
                .headers().frameOptions().disable()

        .and()
                .authorizeRequests()
                .antMatchers("/members/join", "/members/login", "/weather/**").permitAll()
                .antMatchers("/posts/create").hasRole("USER")
                .antMatchers("/members/**").hasRole("USER")
                .anyRequest().authenticated()
        .and()

                .addFilterBefore(new VueLoginProcessingFilter(authenticationManagerBean(), authenticationSuccessHandler, authenticationFailureHandler), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        http
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
    }


    //    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
//
//        http
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/members/join", "/members/login").permitAll()
//                .anyRequest().authenticated()
//        .and()
//                .addFilterBefore(new VueLoginProcessingFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
}
