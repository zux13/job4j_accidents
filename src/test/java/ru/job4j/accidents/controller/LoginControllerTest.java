package ru.job4j.accidents.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test-mock")
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final CsrfToken CSRF_TOKEN =
            new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", "test-token");

    @Test
    void whenLoginPageWithoutParamsThenNoErrorMessage() throws Exception {
        mockMvc.perform(get("/login")
                        .requestAttr(CsrfToken.class.getName(), CSRF_TOKEN))
                .andExpect(status().isOk())
                .andExpect(view().name("/users/login"))
                .andExpect(model().attribute("_csrf", CSRF_TOKEN))
                .andExpect(model().attributeDoesNotExist("errorMessage"));
    }

    @Test
    void whenLoginPageWithErrorThenModelHasErrorMessage() throws Exception {
        mockMvc.perform(get("/login?error=true")
                        .requestAttr(CsrfToken.class.getName(), CSRF_TOKEN))
                .andExpect(status().isOk())
                .andExpect(view().name("/users/login"))
                .andExpect(model().attribute("errorMessage", "Username or Password is incorrect !!"));
    }

    @Test
    void whenLoginPageWithLogoutThenModelHasLogoutMessage() throws Exception {
        mockMvc.perform(get("/login?logout=true")
                        .requestAttr(CsrfToken.class.getName(), CSRF_TOKEN))
                .andExpect(status().isOk())
                .andExpect(view().name("/users/login"))
                .andExpect(model().attribute("errorMessage", "You have been successfully logged out !!"));
    }

    @Test
    @WithMockUser(username = "testUser")
    void whenLogoutThenRedirectToLoginWithLogoutParam() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("testUser", "password")
        );

        mockMvc.perform(get("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout=true"));
    }
}
