package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Rule;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
@AllArgsConstructor
public class JdbcTemplateRuleRepository implements RuleRepository {
    private final JdbcTemplate jdbc;

    private final RowMapper<Rule> rowMapper = (rs, rowNum) -> {
        Rule rule = new Rule();
        rule.setId(rs.getInt("id"));
        rule.setName(rs.getString("name"));
        return rule;
    };

    @Override
    public Rule create(Rule rule) {
        Integer id = jdbc.queryForObject("""
            INSERT INTO rule(name)
            VALUES (?)
            RETURNING id
            """, Integer.class,
                rule.getName()
        );
        rule.setId(id);
        return rule;
    }

    @Override
    public Optional<Rule> get(int id) {
        List<Rule> rules = jdbc.query(
                "SELECT id, name FROM rule WHERE id = ?",
                rowMapper,
                id
        );
        return rules.stream().findFirst();
    }

    @Override
    public void update(Rule rule) {
        jdbc.update(
                "UPDATE rule SET name = ? WHERE id = ?",
                rule.getName(),
                rule.getId()
        );
    }

    @Override
    public void delete(int id) {
        jdbc.update("DELETE FROM rule WHERE id = ?", id);
    }

    @Override
    public List<Rule> findAll() {
        return jdbc.query("SELECT id, name FROM rule ORDER BY id", rowMapper);
    }
}
