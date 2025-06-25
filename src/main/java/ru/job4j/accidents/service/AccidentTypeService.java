package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.repository.SpringDataAccidentTypeRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class AccidentTypeService {

    private final SpringDataAccidentTypeRepository accidentTypeRepository;

    public AccidentType create(AccidentType accidentType) {
        return accidentTypeRepository.save(accidentType);
    }

    public AccidentType get(int id) {
        return accidentTypeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("AccidentType with id " + id + " not found"));
    }

    public void update(AccidentType accidentType) {
        if (get(accidentType.getId()) != null) {
            accidentTypeRepository.save(accidentType);
        }
    }

    public void delete(int id) {
        accidentTypeRepository.deleteById(id);
    }

    public List<AccidentType> findAll() {
        return (List<AccidentType>) accidentTypeRepository.findAll();
    }
}
