package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.AccidentType;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class JdbcTemplateAccidentTypeRepository implements AccidentTypeRepository {
    private final JdbcTemplate jdbc;

    private final RowMapper<AccidentType> accidentTypeRowMapper = (rs, rowNum) -> {
        AccidentType type = new AccidentType();
        type.setId(rs.getInt("id"));
        type.setName(rs.getString("name"));
        return type;
    };

    @Override
    public AccidentType create(AccidentType accidentType) {
        Integer id = jdbc.queryForObject("""
            INSERT INTO accident_type(name)
            VALUES (?)
            RETURNING id
            """, Integer.class,
                accidentType.getName()
        );
        accidentType.setId(id);
        return accidentType;
    }

    @Override
    public Optional<AccidentType> get(int id) {
        List<AccidentType> result = jdbc.query(
                "SELECT id, name FROM accident_type WHERE id = ?",
                accidentTypeRowMapper,
                id
        );
        return result.stream().findFirst();
    }

    @Override
    public void update(AccidentType accidentType) {
        jdbc.update(
                "UPDATE accident_type SET name = ? WHERE id = ?",
                accidentType.getName(),
                accidentType.getId()
        );
    }

    @Override
    public void delete(int id) {
        jdbc.update("DELETE FROM accident_type WHERE id = ?", id);
    }

    @Override
    public List<AccidentType> findAll() {
        return jdbc.query(
                "SELECT id, name FROM accident_type",
                accidentTypeRowMapper
        );
    }
}
