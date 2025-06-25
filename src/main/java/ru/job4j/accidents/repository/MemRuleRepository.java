package ru.job4j.accidents.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Rule;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class MemRuleRepository implements RuleRepository {

    private final AtomicInteger currentId = new AtomicInteger(0);
    private final ConcurrentHashMap<Integer, Rule> store = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        create(new Rule(1, "Статья. 1"));
        create(new Rule(2, "Статья. 2"));
        create(new Rule(3, "Статья. 3"));
    }

    @Override
    public Rule create(Rule rule) {
        rule.setId(currentId.incrementAndGet());
        store.put(rule.getId(), rule);
        return rule;
    }

    @Override
    public Optional<Rule> get(int id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void update(Rule rule) {
        if (rule.getId() == 0) {
            create(rule);
        } else if (store.containsKey(rule.getId())) {
            store.put(rule.getId(), rule);
        }
    }

    @Override
    public void delete(int id) {
        store.remove(id);
    }

    @Override
    public List<Rule> findAll() {
        return store.values()
                .stream()
                .toList();
    }
}
