package me.borebash.rest.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import me.borebash.rest.accounts.AccountService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // WebSecurityConfigurerAdapter 상속하는 순간 Springboot가 제공하는 spring security설정은 더 이상 적용되지 않음
    
    // UserDetails Service
    @Autowired
    AccountService accountService; 
    
    @Autowired
    PasswordEncoder passwordEncoder;

    // Oauth Token Store - In Memory TokenStore에 저장
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    // Authentication Manager를 Bean으로 등록
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // Authentication Manager를 어떻게 만들 것 인가
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService)
            .passwordEncoder(passwordEncoder);
    }

   // Filter의 적용여부
   // Spring Security Filter를 적용하지 않은 부분에 대해서는 원천적으로 차단
   // Servlet이 가지고있는 PathRequest에서 springboot에서 제공해주는 static resource들에 대한 기본 위치는 spring security가 적용되지 않도록
   @Override
   public void configure(WebSecurity web) throws Exception {
       web.ignoring().mvcMatchers("/docs/index.html");
       web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
   }

//    // HttpSecurity를 적용하는 경우
//    // 일단 Spring Security에 들어와서 Request의 인증이 annoymous(== ignore)
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .mvcMatchers("/docs/index.html").anonymous()
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).anonymous();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .anonymous() // 익명 사용자를 허용
            .and()
            .formLogin() // Form Authentication을 사용
            .and()
            .authorizeRequests() 
                .mvcMatchers(HttpMethod.GET, "/api/**").anonymous() // GET /api/**의 모든 요청에 Anonymous를 허용
                .anyRequest().authenticated(); // 나머지는 인증이 필요
    }

}