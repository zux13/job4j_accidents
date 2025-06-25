package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.AccidentType;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
@AllArgsConstructor
public class HibernateAccidentTypeRepository implements AccidentTypeRepository {

    private final SessionFactory sf;

    @Override
    public AccidentType create(AccidentType accidentType) {
        try (var session = sf.openSession()) {
            session.beginTransaction();
            session.persist(accidentType);
            session.getTransaction().commit();
            return accidentType;
        }
    }

    @Override
    public Optional<AccidentType> get(int id) {
        try (var session = sf.openSession()) {
            return Optional.ofNullable(session.find(AccidentType.class, id));
        }
    }

    @Override
    public void update(AccidentType accidentType) {
        try (var session = sf.openSession()) {
            session.beginTransaction();
            session.merge(accidentType);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        try (var session = sf.openSession()) {
            session.beginTransaction();
            var type = session.find(AccidentType.class, id);
            if (type != null) {
                session.remove(type);
            }
            session.getTransaction().commit();
        }
    }

    @Override
    public List<AccidentType> findAll() {
        try (var session = sf.openSession()) {
            return session.createQuery("from AccidentType", AccidentType.class).list();
        }
    }
}
