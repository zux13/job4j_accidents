package ru.job4j.accidents.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ru.job4j.accidents.config.TestConfiguration;
import ru.job4j.accidents.model.Authority;
import ru.job4j.accidents.repository.SpringDataAuthorityRepository;
import ru.job4j.accidents.repository.SpringDataUserRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegController.class)
@Import(TestConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class RegControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringDataUserRepository userRepository;

    @Autowired
    private SpringDataAuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Test
    void whenGetRegisterPageThenReturnsView() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("/users/register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
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
