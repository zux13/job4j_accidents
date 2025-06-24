package ru.job4j.accidents.repository;

import ru.job4j.accidents.model.AccidentType;

import java.util.List;
import java.util.Optional;

public interface AccidentTypeRepository {

    AccidentType create(AccidentType accidentType);

    Optional<AccidentType> get(int id);

    void update(AccidentType accidentType);

    void delete(int id);

    List<AccidentType> findAll();

}
