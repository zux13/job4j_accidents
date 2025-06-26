package ru.job4j.accidents.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ru.job4j.accidents.config.TestConfig;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.service.AccidentService;
import ru.job4j.accidents.service.AccidentTypeService;
import ru.job4j.accidents.service.RuleService;

import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(AccidentController.class)
@Import(TestConfig.class)
@ActiveProfiles("test-mock")
class AccidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccidentService accidentService;

    @Autowired
    private AccidentTypeService accidentTypeService;

    @Autowired
    private RuleService ruleService;

    @Test
    @WithMockUser
    void whenGetCreatePageThenReturnView() throws Exception {
        Mockito.when(ruleService.findAll()).thenReturn(List.of());
        Mockito.when(accidentTypeService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("/accidents/create"))
                .andExpect(model().attributeExists("rules"))
                .andExpect(model().attributeExists("types"));
    }

    @Test
    @WithMockUser
    void whenPostSaveThenRedirect() throws Exception {
        AccidentType type = new AccidentType(1, "Type1");
        Set<Rule> rules = Set.of(new Rule(1, "R1"), new Rule(2, "R2"));

        Mockito.when(accidentTypeService.get(1)).thenReturn(type);
        Mockito.when(ruleService.findByIds(List.of(1, 2))).thenReturn(rules);

        mockMvc.perform(post("/save")
                        .param("name", "Accident1")
                        .param("text", "Text")
                        .param("address", "Somewhere")
                        .param("type.id", "1")
                        .param("rIds", "1", "2")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));
    }

    @Test
    @WithMockUser
    void whenGetEditPageThenReturnView() throws Exception {
        int id = 1;
        Accident accident = new Accident();
        accident.setId(id);
        Mockito.when(accidentService.get(id)).thenReturn(accident);
        Mockito.when(ruleService.findAll()).thenReturn(List.of());
        Mockito.when(accidentTypeService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/edit").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/accidents/edit"))
                .andExpect(model().attributeExists("accident"))
                .andExpect(model().attributeExists("rules"))
                .andExpect(model().attributeExists("types"));
    }

    @Test
    @WithMockUser
    void whenPostUpdateThenRedirect() throws Exception {
        AccidentType type = new AccidentType(1, "Updated Type");
        Mockito.when(accidentTypeService.get(1)).thenReturn(type);
        Mockito.when(ruleService.findByIds(List.of(1))).thenReturn(Set.of(new Rule(1, "Updated Rule")));

        mockMvc.perform(post("/update")
                        .param("id", "1")
                        .param("name", "Updated Accident")
                        .param("text", "Updated text")
                        .param("address", "Updated place")
                        .param("type.id", "1")
                        .param("rIds", "1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));
    }

    @Test
    @WithMockUser
    void whenGetDeleteThenRedirect() throws Exception {
        mockMvc.perform(get("/delete").param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));
        Mockito.verify(accidentService).delete(1);
    }
}
