package me.borebash.rest.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;


// Resource Server랑 연동이 되서 사용이 된다.
// 외부 요청이 Resource에 접근을 할 때, 토큰을 유효한가를 확인 즉 접근 제한
@Configuration
@EnableResourceServer
class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("event");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .anonymous() 
                .and().authorizeRequests().mvcMatchers(HttpMethod.GET, "/api/**").permitAll()
                .anyRequest().authenticated()
            .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler()); // 권한이 없는 경우, 인증이 잘못된 경우 -> 접근 권한이 없는 경우 
    }
}