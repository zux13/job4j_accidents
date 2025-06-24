package ru.job4j.accidents.repository;

import ru.job4j.accidents.model.Accident;

import java.util.Collection;
import java.util.Optional;

public interface AccidentRepository {

    Accident save(Accident accident);

    Optional<Accident> get(int id);

    void update(Accident accident);

    void delete(int id);

    Collection<Accident> findAll();
}
