package ru.job4j.accidents.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.job4j.accidents.repository.SpringDataAuthorityRepository;
import ru.job4j.accidents.repository.SpringDataUserRepository;
import ru.job4j.accidents.service.AccidentService;
import ru.job4j.accidents.service.AccidentTypeService;
import ru.job4j.accidents.service.RuleService;

@Configuration
public class TestConfiguration {

    @Bean
    public AccidentService accidentService() {
        return Mockito.mock(AccidentService.class);
    }

    @Bean
    public AccidentTypeService accidentTypeService() {
        return Mockito.mock(AccidentTypeService.class);
    }

    @Bean
    public RuleService ruleService() {
        return Mockito.mock(RuleService.class);
    }

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
