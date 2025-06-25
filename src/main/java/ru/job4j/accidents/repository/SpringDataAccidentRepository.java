package ru.job4j.accidents.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.accidents.model.Accident;

import java.util.List;

public interface SpringDataAccidentRepository extends CrudRepository<Accident, Integer> {
    @Query("SELECT a FROM Accident a JOIN FETCH a.rules")
    List<Accident> findAll();
}
