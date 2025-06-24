package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.repository.AccidentTypeRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class AccidentTypeService {
    private final AccidentTypeRepository accidentTypeRepository;

    public AccidentType create(AccidentType accidentType) {
        return accidentTypeRepository.create(accidentType);
    }

    public AccidentType get(int id) {
        return accidentTypeRepository.get(id)
                .orElseThrow(() -> new NoSuchElementException("AccidentType with id " + id + "not found"));
    }

    public void update(AccidentType accidentType) {
        accidentTypeRepository.update(accidentType);
    }

    public void delete(int id) {
        accidentTypeRepository.delete(id);
    }

    public List<AccidentType> findAll() {
        return accidentTypeRepository.findAll();
    }

}
