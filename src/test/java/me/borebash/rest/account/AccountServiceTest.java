package me.borebash.rest.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import me.borebash.rest.accounts.Account;
import me.borebash.rest.accounts.AccountRole;
import me.borebash.rest.accounts.AccountService;
import me.borebash.rest.common.BaseControllerTest;

@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;
    
    //@Autowired
    //AccountRepository accountRepository;

    @Test
    public void loadUserByUsername() {
        // Given
        String username = "test@email.com";
        String password = "test";
        Account account = Account.builder()
                            .email(username)
                            .password(password)
                            .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                            .build();

        this.accountService.saveAccount(account);

        // When
        UserDetailsService userDetailsService = accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Then
        assertThat(this.passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
    }

    // @Test(expected = UsernameNotFoundException)
    @Test 
    public void loadUserByUsername_Fail() {
        
        assertThrows(UsernameNotFoundException.class, () -> accountService.loadUserByUsername("test@email.com"));
        
    }
}