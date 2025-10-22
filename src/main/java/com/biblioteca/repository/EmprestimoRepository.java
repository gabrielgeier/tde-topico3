package com.biblioteca.repository;

import com.biblioteca.entity.Emprestimo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
public class EmprestimoRepository {
    @PersistenceContext
    EntityManager em;

    public List<Emprestimo> findAll() {
        return em.createQuery(
                "SELECT e FROM Emprestimo e LEFT JOIN FETCH e.livro l LEFT JOIN FETCH l.autor", Emprestimo.class)
                .getResultList();
    }

    public List<Emprestimo> findAtivos() {
        return em.createQuery(
                "SELECT e FROM Emprestimo e LEFT JOIN FETCH e.livro WHERE e.dataDevolucao IS NULL", Emprestimo.class)
                .getResultList();
    }

    public long count() {
        return em.createQuery("SELECT COUNT(e) FROM Emprestimo e", Long.class).getSingleResult();
    }

    public long countAtivos() {
        return em.createQuery("SELECT COUNT(e) FROM Emprestimo e WHERE e.dataDevolucao IS NULL", Long.class)
                .getSingleResult();
    }

    public Emprestimo findById(Long id) {
        return em.find(Emprestimo.class, id);
    }

    public void persist(Emprestimo e) {
        em.persist(e);
    }

    public void remove(Emprestimo e) {
        em.remove(e);
    }
}