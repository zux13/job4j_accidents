package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;

import java.util.Collection;
import java.util.Optional;

@Repository
@Primary
@AllArgsConstructor
public class HibernateAccidentRepository implements AccidentRepository {

    private final SessionFactory sf;

    @Override
    public Accident save(Accident accident) {
        try (var session = sf.openSession()) {
            session.beginTransaction();
            session.persist(accident);
            session.getTransaction().commit();
            return accident;
        }
    }

    @Override
    public Optional<Accident> get(int id) {
        try (var session = sf.openSession()) {
            return session.createQuery("""
                            select distinct a from Accident a
                            join fetch a.type
                            left join fetch a.rules
                            where a.id = :id
                            """, Accident.class)
                    .setParameter("id", id)
                    .uniqueResultOptional();
        }
    }

    @Override
    public void update(Accident accident) {
        try (var session = sf.openSession()) {
            session.beginTransaction();
            session.merge(accident);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        try (var session = sf.openSession()) {
            session.beginTransaction();
            Accident accident = session.find(Accident.class, id);
            if (accident != null) {
                session.remove(accident);
            }
            session.getTransaction().commit();
        }
    }

    @Override
    public Collection<Accident> findAll() {
        try (var session = sf.openSession()) {
            return session.createQuery("""
                            select distinct a from Accident a
                            join fetch a.type
                            left join fetch a.rules
                            """, Accident.class)
                    .list();
        }
    }
}
