package me.borebash.rest.configs;

import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import me.borebash.rest.accounts.Account;
import me.borebash.rest.accounts.AccountRole;
import me.borebash.rest.accounts.AccountService;
import me.borebash.rest.common.AppProperties;

@Configuration
public class AppConfig {
    
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder () {
        // 다양한 Encoding타입을 지원하는 Password
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Autowired
            AccountService accountService;

            @Autowired
            AppProperties appProperties;

            @Override
            public void run(ApplicationArguments args) throws Exception{
                Account account = Account.builder()
                                        .email(appProperties.getAdminUsername())
                                        .password(appProperties.getAdminPassword())
                                        .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                                        .build();
                this.accountService.saveAccount(account);

                Account user = Account.builder()
                                        .email(appProperties.getUserUsername())
                                        .password(appProperties.getUserPassword())
                                        .roles(Set.of(AccountRole.USER))
                                        .build();
                this.accountService.saveAccount(user);
            }
        };
    }

}