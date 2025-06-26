package ru.job4j.accidents.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.repository.*;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AccidentControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringDataAccidentRepository accidentRepository;

    @Autowired
    private SpringDataAccidentTypeRepository typeRepository;

    @Autowired
    private SpringDataRuleRepository ruleRepository;

    @Test
    @WithMockUser
    void whenCreateAccidentThenItIsSaved() throws Exception {
        AccidentType type = typeRepository.save(new AccidentType(0, "Type IT"));
        Rule rule = ruleRepository.save(new Rule(0, "Rule IT"));

        mockMvc.perform(post("/save")
                        .param("name", "Test Accident")
                        .param("text", "Test Description")
                        .param("address", "Test Address")
                        .param("type.id", String.valueOf(type.getId()))
                        .param("rIds", String.valueOf(rule.getId()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));

        var found = accidentRepository.findAll()
                .stream()
                .filter(a -> a.getName().equals("Test Accident"))
                .findFirst();

        assertThat(found).isPresent();
        assertThat(found.get().getType().getName()).isEqualTo("Type IT");
        assertThat(found.get().getRules()).extracting(Rule::getName).containsExactly("Rule IT");
    }

    @Test
    @WithMockUser
    void whenEditAccidentThenReturnsFormWithData() throws Exception {
        AccidentType type = typeRepository.save(new AccidentType(0, "Type Edit"));
        Accident accident = accidentRepository.save(new Accident(0, "Edit", "Text", "Addr", type, Set.of()));

        mockMvc.perform(get("/edit?id=" + accident.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("/accidents/edit"))
                .andExpect(model().attributeExists("accident"))
                .andExpect(model().attributeExists("rules"))
                .andExpect(model().attributeExists("types"));
    }

    @Test
    @WithMockUser
    void whenDeleteAccidentThenItIsRemoved() throws Exception {
        AccidentType type = typeRepository.save(new AccidentType(0, "Type Del"));
        Accident accident = accidentRepository.save(new Accident(0, "ToDelete", "text", "addr", type, Set.of()));

        mockMvc.perform(get("/delete?id=" + accident.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));

        assertThat(accidentRepository.findById(accident.getId())).isEmpty();
    }
}
