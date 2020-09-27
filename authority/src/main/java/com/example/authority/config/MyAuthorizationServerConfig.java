package com.example.authority.config;

import com.example.authority.properties.ClientLoadProperties;
import com.example.authority.properties.ClientProperties;
import com.example.authority.service.MyUserDetailsServiceImpl;
import com.google.common.net.HttpHeaders;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author xuxiaojie
 * @deprecated 认证服务器
 * @date 2020-09ss-27
 */

@Configuration
@EnableAuthorizationServer
public class MyAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;
    @Autowired
    private MyUserDetailsServiceImpl userDetailsService;
    @Autowired
    private RedisConnectionFactory connectionFactory;
    @Resource
    private ClientLoadProperties clientLoadProperties;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 定义token的存储方式
     *
     * @return TokenStore
     */
    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(connectionFactory);
    }

    /**
     * 定义令牌端点上的安全性约 束
     *
     * @param oauthServer oauthServer defines the security constraints on the token endpoint.
     * @throws Exception exception
     */
    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            /**
             * 配置oauth2服务跨域
             */
         CorsConfigurationSource source = new CorsConfigurationSource() {
         @Override
          public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                 CorsConfiguration corsConfiguration = new CorsConfiguration();
                 corsConfiguration.addAllowedHeader("*");
                 corsConfiguration.addAllowedOrigin(request.getHeader(HttpHeaders.ORIGIN));
                 corsConfiguration.addAllowedMethod("*");
                 corsConfiguration.setAllowCredentials(true);
                 corsConfiguration.setMaxAge(3600L);
                 return corsConfiguration;
            }
         };

        oauthServer.tokenKeyAccess("permitAll()")//配置token请求不进行拦截
                .checkTokenAccess("isAuthenticated()");//验证通过返回token信息
        oauthServer.allowFormAuthenticationForClients();

    }


    /**
     * 用于定义客户端详细信息服务的配置程序。可以初始化客户端详细信息，也可以只引用现有商店。
     *
     * @param clients a configurer that defines the client details service. Client details can be initialized, or you can just refer to an existing store.
     * @throws Exception exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
        if (ArrayUtils.isNotEmpty(clientLoadProperties.getClients())) {
            for (ClientProperties config : clientLoadProperties.getClients()) {
                builder
                        //设置客户端和密码
                        .withClient(config.getClientId()).secret(bCryptPasswordEncoder.encode(config.getClientSecret()))
                        //设置token有效期
                        .accessTokenValiditySeconds(7 * 24 * 3600)
                        //设置refreshToken有效期
                        .refreshTokenValiditySeconds(7 * 24 * 3600)
                        //支持的认证方式
                        .authorizedGrantTypes("refresh_token", "authorization_code", "password").autoApprove(false)
                        //授权域
                        .scopes("all");
            }
        }

    }

    /**
     * 定义授权和令牌端点以及令牌服务
     *
     * @param endpoints defines the authorization and token endpoints and the token services.
     * @throws Exception exception
     */
    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //指定认证管理器
                .authenticationManager(authenticationManager)
                //用户账号密码认证
                .userDetailsService(userDetailsService)
                // refresh_token
                .reuseRefreshTokens(false)
                //指定token存储位置
                .tokenStore(tokenStore());
    }
}
