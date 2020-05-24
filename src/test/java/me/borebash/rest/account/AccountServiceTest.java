package me.borebash.rest.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import me.borebash.rest.accounts.Account;
import me.borebash.rest.accounts.AccountRepository;
import me.borebash.rest.accounts.AccountRole;
import me.borebash.rest.accounts.AccountService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void loadUserByUsername() {
        // Given
        final String username = "test@email.com";
        final String password = "test";
        final Account account = Account.builder()
                            .email(username)
                            .password(password)
                            .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                            .build();

        accountRepository.save(account);

        // When
        final UserDetailsService userDetailsService = accountService;
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Then
        assertThat(userDetails.getPassword()).isEqualTo(password);
    }

    // @Test(expected = UsernameNotFoundException)
    @Test 
    public void loadUserByUsername_Fail() {
        // Given & Expected
        String username = "test@email.com";
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage(Matchers.containsString(username));

        // When & Given 
        accountService.loadUserByUsername(username);
        
    }
}