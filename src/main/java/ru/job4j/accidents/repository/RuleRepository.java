package ru.job4j.accidents.repository;

import ru.job4j.accidents.model.Rule;

import java.util.List;
import java.util.Optional;

public interface RuleRepository {

    Rule create(Rule rule);

    Optional<Rule> get(int id);

    void update(Rule rule);

    void delete(int id);

    List<Rule> findAll();

}
