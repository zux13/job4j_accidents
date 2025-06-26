package ru.job4j.accidents.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.accidents.config.TestConfiguration;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.service.AccidentService;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IndexController.class)
@Import(TestConfiguration.class)
class IndexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccidentService accidentService;

    @Test
    @WithMockUser(username = "testUser")
    void whenGetIndexThenModelContainsUsernameAndAccidents() throws Exception {
        var username = "testUser";

        var type = new AccidentType(1, "Type A");
        var rule = new Rule(1, "Rule 1");
        var accident = new Accident(1, "Accident A", "Some text", "Some address", type, Set.of(rule));

        when(accidentService.findAll()).thenReturn(List.of(accident));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("username", username))
                .andExpect(model().attribute("accidents", List.of(accident)));
    }
}
