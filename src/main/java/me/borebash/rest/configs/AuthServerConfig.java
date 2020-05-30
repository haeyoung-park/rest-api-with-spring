package me.borebash.rest.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import me.borebash.rest.accounts.AccountService;
import me.borebash.rest.common.AppProperties;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    
    @Autowired
    PasswordEncoder passwordEncoder;

    // 유저 인증정보 보유
    @Autowired
    AuthenticationManager authenticationManager; 

    @Autowired
    AccountService accountService;

    @Autowired
    TokenStore tokenStore;

    @Autowired
    AppProperties appProperties;

    // Client Secret을 확인할 때 Encoder를 사용
    @Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.passwordEncoder(passwordEncoder);
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
        // Refresh Token은 OAuth Token을 발급받을 때, Refresh Token도 같이 발급
        // Refresh Token을 가지고 새로운 Access Token을 발급받는 Grant Type
                    .withClient(appProperties.getClientId())
                    .authorizedGrantTypes("password", "refresh_token") 
                    .scopes("read", "write")
                    .secret(this.passwordEncoder.encode(appProperties.getClientSecret()))
                    .accessTokenValiditySeconds(10 * 60)
                    .refreshTokenValiditySeconds(6 * 10 * 60)
                    ;
    }

    // Authentication Manager, Token Store, UserDetails를 설정
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(accountService)
                .tokenStore(tokenStore);
    }

}