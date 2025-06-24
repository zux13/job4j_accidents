package ru.job4j.accidents.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.AccidentType;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class MemAccidentTypeRepository implements AccidentTypeRepository {

    private final AtomicInteger currentId = new AtomicInteger(0);
    private final ConcurrentHashMap<Integer, AccidentType> store = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        create(new AccidentType(0, "Две машины"));
        create(new AccidentType(0, "Машина и человек"));
        create(new AccidentType(0, "Машина и велосипед"));
    }

    @Override
    public AccidentType create(AccidentType accidentType) {
        accidentType.setId(currentId.incrementAndGet());
        store.put(accidentType.getId(), accidentType);
        return accidentType;
    }

    @Override
    public Optional<AccidentType> get(int id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void update(AccidentType accidentType) {
        if (accidentType.getId() == 0) {
            create(accidentType);
        } else if (store.containsKey(accidentType.getId())) {
            store.put(accidentType.getId(), accidentType);
        }
    }

    @Override
    public void delete(int id) {
        store.remove(id);
    }

    @Override
    public List<AccidentType> findAll() {
        return store.values()
                .stream()
                .toList();
    }
}
