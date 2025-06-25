package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;

import java.util.*;

@Repository
@AllArgsConstructor
public class JdbcTemplateAccidentRepository implements AccidentRepository {
    private final JdbcTemplate jdbc;

    private final RowMapper<Accident> accidentRowMapper = (rs, rowNum) -> {
        Accident accident = new Accident();
        accident.setId(rs.getInt("id"));
        accident.setName(rs.getString("name"));
        accident.setText(rs.getString("text"));
        accident.setAddress(rs.getString("address"));
        accident.setType(new AccidentType(rs.getInt("type_id"), rs.getString("type_name")));
        return accident;
    };

    @Override
    public Accident save(Accident accident) {
        Integer id = jdbc.queryForObject("""
            INSERT INTO accident(name, text, address, type_id)
            VALUES (?, ?, ?, ?)
            RETURNING id
        """, Integer.class,
                accident.getName(),
                accident.getText(),
                accident.getAddress(),
                accident.getType().getId()
        );
        accident.setId(id);
        saveRules(accident);
        return accident;
    }

    @Override
    public Optional<Accident> get(int id) {
        List<Accident> result = jdbc.query("""
            SELECT a.id, a.name, a.text, a.address, a.type_id, t.name AS type_name
            FROM accident a
            JOIN accident_type t ON a.type_id = t.id
            WHERE a.id = ?
        """, accidentRowMapper, id);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        Accident accident = result.get(0);
        accident.setRules(loadRules(accident.getId()));
        return Optional.of(accident);
    }

    @Override
    public void update(Accident accident) {
        jdbc.update("""
            UPDATE accident SET name = ?, text = ?, address = ?, type_id = ?
            WHERE id = ?
        """, accident.getName(), accident.getText(), accident.getAddress(), accident.getType().getId(), accident.getId());
        jdbc.update("DELETE FROM accident_rule WHERE accident_id = ?", accident.getId());
        saveRules(accident);
    }

    @Override
    public void delete(int id) {
        jdbc.update("DELETE FROM accident_rule WHERE accident_id = ?", id);
        jdbc.update("DELETE FROM accident WHERE id = ?", id);
    }

    @Override
    public Collection<Accident> findAll() {
        List<Accident> accidents = jdbc.query("""
            SELECT a.id, a.name, a.text, a.address, a.type_id, t.name AS type_name
            FROM accident a
            JOIN accident_type t ON a.type_id = t.id
        """, accidentRowMapper);
        for (Accident accident : accidents) {
            accident.setRules(loadRules(accident.getId()));
        }
        return accidents;
    }

    private void saveRules(Accident accident) {
        if (accident.getRules() != null) {
            for (Rule rule : accident.getRules()) {
                jdbc.update("INSERT INTO accident_rule(accident_id, rule_id) VALUES (?, ?)",
                        accident.getId(), rule.getId());
            }
        }
    }

    private Set<Rule> loadRules(int accidentId) {
        return new HashSet<>(jdbc.query("""
            SELECT r.id, r.name FROM rule r
            JOIN accident_rule ar ON r.id = ar.rule_id
            WHERE ar.accident_id = ?
        """, (rs, rowNum) -> new Rule(rs.getInt("id"), rs.getString("name")), accidentId));
    }
}
