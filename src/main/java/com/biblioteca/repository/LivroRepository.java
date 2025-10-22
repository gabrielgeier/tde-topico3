package com.biblioteca.repository;

import com.biblioteca.entity.Livro;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
public class LivroRepository {
    @PersistenceContext
    EntityManager em;

    public List<Livro> findAll() {
        return em.createQuery("SELECT l FROM Livro l LEFT JOIN FETCH l.autor", Livro.class).getResultList();
    }

    public long count() {
        return em.createQuery("SELECT COUNT(l) FROM Livro l", Long.class).getSingleResult();
    }

    public long countByDisponivel(boolean d) {
        return em.createQuery("SELECT COUNT(l) FROM Livro l WHERE l.disponivel = :d", Long.class).setParameter("d", d)
                .getSingleResult();
    }

    public Livro findById(Long id) {
        return em.find(Livro.class, id);
    }

    public void persist(Livro l) {
        em.persist(l);
    }

    public void remove(Livro l) {
        em.remove(l);
    }
}