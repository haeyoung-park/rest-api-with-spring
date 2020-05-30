package me.borebash.rest.configs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.borebash.rest.accounts.Account;
import me.borebash.rest.accounts.AccountRole;
import me.borebash.rest.accounts.AccountService;
import me.borebash.rest.common.AppProperties;
import me.borebash.rest.common.BaseControllerTest;
import me.borebash.rest.common.TestDescription;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class AuthServersConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AppProperties appProperties;

    @Test
    @TestDescription("인증 토큰 발급")
    public void getAuthentiacationToken() throws Exception {
        // Given
        String clinetID = appProperties.getClientId();
        String clientSecret = appProperties.getClientSecret();

        // When & Then
        // 인증을 하는 6가지 방법 중에 Spring Oauth2가 제공하는 2가지 방법(Refresh Token, Grant Type : Password)
        // Grant Type : Token을 받아오는 방법
        mockMvc.perform(post("/oauth/token")
                    .with(httpBasic(clinetID, clientSecret)) // Basic Auth의 헤더
                    .param("username", appProperties.getUserUsername())
                    .param("password", appProperties.getUserPassword())
                    .param("grant_type", "password")
                    ) 
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("access_token").exists())
            ;
    }
}