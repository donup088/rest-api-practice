package study.restapi.configs;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import study.restapi.accounts.Account;
import study.restapi.accounts.AccountRepository;
import study.restapi.accounts.AccountRole;
import study.restapi.accounts.AccountService;
import study.restapi.common.AppProperties;
import study.restapi.common.BaseControllerTest;
import study.restapi.common.TestDescription;

import java.util.Set;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AppProperties appProperties;

    @Autowired
    AccountRepository accountRepository;

    @Before
    public void setup(){
        accountRepository.deleteAll();
    }

    @Test
    @TestDescription("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception {
        Account account = Account.builder()
                .email(appProperties.getAdminUsername())
                .password(appProperties.getAdminPassword())
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        accountService.saveAccount(account);

        mvc.perform(post("/oauth/token")
                .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
                .param("username", appProperties.getAdminUsername())
                .param("password", appProperties.getAdminPassword())
                .param("grant_type", "password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
    }
}