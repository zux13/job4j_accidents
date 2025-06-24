package ru.job4j.accidents.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class MemAccidentRepository implements AccidentRepository {
    private final AtomicInteger currentId = new AtomicInteger(0);
    private final ConcurrentHashMap<Integer, Accident> store = new ConcurrentHashMap<>();
    private final AccidentTypeRepository accidentTypeRepository;

    @Autowired
    MemAccidentRepository(AccidentTypeRepository accidentTypeRepository) {
        this.accidentTypeRepository = accidentTypeRepository;
    }

    @PostConstruct
    public void init() {
        List<AccidentType> accidentTypes = accidentTypeRepository.findAll();
        save(new Accident(0, "А123БВ", "Проезд на красный", "ул. Пушкина, д.10", accidentTypes.get(0)));
        save(new Accident(0, "О456ЕР", "Парковка на тротуаре", "пр. Ленина, д.7", accidentTypes.get(1)));
        save(new Accident(0, "К789МН", "Опасное вождение", "ул. Гагарина, д.5", accidentTypes.get(2)));
    }

    @Override
    public Accident save(Accident accident) {
        accident.setId(currentId.incrementAndGet());
        store.put(accident.getId(), accident);
        return accident;

    }

    @Override
    public Optional<Accident> get(int id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void update(Accident accident) {
        if (accident.getId() == 0) {
            save(accident);
        } else if (store.containsKey(accident.getId())) {
            store.put(accident.getId(), accident);
        }
    }

    @Override
    public void delete(int id) {
        store.remove(id);
    }

    @Override
    public Collection<Accident> findAll() {
        return store.values();
    }
}
