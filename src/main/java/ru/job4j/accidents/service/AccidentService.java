package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.repository.SpringDataAccidentRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class AccidentService {

    private SpringDataAccidentRepository accidentRepository;

    public Accident save(Accident accident) {
        return accidentRepository.save(accident);
    }

    public Accident get(int id) {
        return accidentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Accident with id " + id + " not found"));
    }

    public void update(Accident accident) {
        if (get(accident.getId()) != null) {
            accidentRepository.save(accident);
        }
    }

    public void delete(int id) {
        accidentRepository.deleteById(id);
    }

    public List<Accident> findAll() {
        return (List<Accident>) accidentRepository.findAll();
    }

}
