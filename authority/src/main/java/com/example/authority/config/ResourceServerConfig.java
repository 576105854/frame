package com.example.authority.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;


@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
//                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .requestMatchers().antMatchers("/api/**")
                .and()
                .authorizeRequests()
                // or可以通过access_token访问，但是and不行；经过测试，应该是hasRole()方法出了问题，这里无法通过
//                    .antMatchers("/product/**").access("#oauth2.hasScope('select') and  hasRole('ROLE_ADMIN')")
                .antMatchers("/api/**").authenticated()// 配置api访问控制，必须认证过后才可以访问
                .and()
                .httpBasic();
    }

}
