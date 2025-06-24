package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.repository.AccidentRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class AccidentService {

    private AccidentRepository accidentRepository;

    public Accident save(Accident accident) {
        return accidentRepository.save(accident);
    }

    public Accident get(int id) {
        return accidentRepository.get(id)
                .orElseThrow(() -> new NoSuchElementException("Accident with id " + id + " not found"));
    }

    public void update(Accident accident) {
        accidentRepository.update(accident);
    }

    public void delete(int id) {
        accidentRepository.delete(id);
    }

    public List<Accident> findAll() {
        return accidentRepository.findAll()
                .stream()
                .toList();
    }

}
