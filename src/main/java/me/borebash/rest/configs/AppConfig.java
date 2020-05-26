package me.borebash.rest.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

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

}