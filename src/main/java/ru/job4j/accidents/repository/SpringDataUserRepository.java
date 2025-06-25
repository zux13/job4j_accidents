package ru.job4j.accidents.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.accidents.model.User;

import java.util.Optional;

public interface SpringDataUserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByName(String name);
}
