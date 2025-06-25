package ru.job4j.accidents.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.accidents.model.AccidentType;

public interface SpringDataAccidentTypeRepository extends CrudRepository<AccidentType, Integer> {
}
