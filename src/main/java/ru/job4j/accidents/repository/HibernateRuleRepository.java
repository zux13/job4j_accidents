package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Rule;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
@AllArgsConstructor
public class HibernateRuleRepository implements RuleRepository {

    private final SessionFactory sf;

    @Override
    public Rule create(Rule rule) {
        try (var session = sf.openSession()) {
            session.beginTransaction();
            session.persist(rule);
            session.getTransaction().commit();
            return rule;
        }
    }

    @Override
    public Optional<Rule> get(int id) {
        try (var session = sf.openSession()) {
            return Optional.ofNullable(session.find(Rule.class, id));
        }
    }

    @Override
    public void update(Rule rule) {
        try (var session = sf.openSession()) {
            session.beginTransaction();
            session.merge(rule);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        try (var session = sf.openSession()) {
            session.beginTransaction();
            Rule rule = session.find(Rule.class, id);
            if (rule != null) {
                session.remove(rule);
            }
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Rule> findAll() {
        try (var session = sf.openSession()) {
            return session.createQuery("from Rule", Rule.class).list();
        }
    }
}
