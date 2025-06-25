package ru.job4j.accidents.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class MemAccidentRepository implements AccidentRepository {
    private final AtomicInteger currentId = new AtomicInteger(0);
    private final ConcurrentHashMap<Integer, Accident> store = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        Accident accident1 = new Accident();
        accident1.setName("А123БВ");
        accident1.setText("Проезд на красный");
        accident1.setAddress("ул. Пушкина, д.10");
        save(accident1);

        Accident accident2 = new Accident();
        accident2.setName("О456ЕP");
        accident2.setText("Парковка на тротуаре");
        accident2.setAddress("пр. Ленина, д.7");
        save(accident2);

        Accident accident3 = new Accident();
        accident3.setName("К789МН");
        accident3.setText("Опасное вождение");
        accident3.setAddress("ул. Гагарина, д.5");
        save(accident3);
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
