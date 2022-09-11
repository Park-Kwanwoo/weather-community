package org.project.weathercommunity.config.security;

import lombok.extern.slf4j.Slf4j;
import org.project.weathercommunity.config.security.filter.VueLoginProcessingFilter;
import org.project.weathercommunity.config.security.handler.VueAccessDeniedHandler;
import org.project.weathercommunity.config.security.handler.VueAuthenticationFailureHandler;
import org.project.weathercommunity.config.security.handler.VueAuthenticationSuccessHandler;
import org.project.weathercommunity.config.security.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
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
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }
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
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/members/join", "/members/login", "/weather").permitAll()
                .antMatchers("/posts/create").hasRole("USER")
                .antMatchers("/members/**").hasRole("USER")
                .anyRequest().authenticated()
        .and()

                .addFilterBefore(new VueLoginProcessingFilter(authenticationManagerBean(), customAuthenticationSuccessHandler(), customAuthenticationFailureHandler()), UsernamePasswordAuthenticationFilter.class);

        http
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint())
                .accessDeniedHandler(customAccessDeniedHandler());
    }

    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new VueAuthenticationEntryPoint();
    }

    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return new VueAccessDeniedHandler();
    }
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new VueAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new VueAuthenticationFailureHandler();
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
