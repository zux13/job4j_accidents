package ru.job4j.accidents.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ru.job4j.accidents.model.Authority;
import ru.job4j.accidents.repository.SpringDataAuthorityRepository;
import ru.job4j.accidents.repository.SpringDataUserRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegController.class)
@Import(RegControllerTest.TestConfig.class)
class RegControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringDataUserRepository userRepository;

    @Autowired
    private SpringDataAuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder encoder;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public SpringDataUserRepository userRepository() {
            return Mockito.mock(SpringDataUserRepository.class);
        }

        @Bean
        public SpringDataAuthorityRepository authorityRepository() {
            return Mockito.mock(SpringDataAuthorityRepository.class);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return Mockito.mock(PasswordEncoder.class);
        }
    }

    @Test
    @WithMockUser
    void whenGetRegisterPageThenReturnsView() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("/users/register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser
    void whenPostRegisterThenUserSavedAndRedirect() throws Exception {
        Authority roleUser = new Authority(1, "ROLE_USER");
        Mockito.when(authorityRepository.findByAuthority("ROLE_USER")).thenReturn(roleUser);
        Mockito.when(encoder.encode("secret")).thenReturn("encoded-secret");

        mockMvc.perform(post("/register")
                        .param("name", "newuser")
                        .param("password", "secret")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        Mockito.verify(userRepository).save(Mockito.argThat(user ->
                user.getName().equals("newuser")
                        && user.getPassword().equals("encoded-secret")
                        && user.isEnabled()
                        && roleUser.equals(user.getAuthority())
        ));
    }
}
