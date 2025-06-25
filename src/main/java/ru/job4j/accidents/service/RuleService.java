package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.repository.RuleRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RuleService {
    private final RuleRepository ruleRepository;

    public Rule create(Rule rule) {
        return ruleRepository.create(rule);
    }

    public Rule get(int id) {
        return ruleRepository.get(id)
                .orElseThrow(() -> new NoSuchElementException("Rule with id " + id + " not found"));
    }

    public void update(Rule rule) {
        ruleRepository.update(rule);
    }

    public void delete(int id) {
        ruleRepository.delete(id);
    }

    public List<Rule> findAll() {
        return ruleRepository.findAll();
    }

    public Set<Rule> findByIds(List<Integer> ids) {
        return ids.stream()
                .map(this::get)
                .collect(Collectors.toSet());
    }
}
