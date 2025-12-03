package com.biblioteca.service;

import com.biblioteca.entity.Autor;
import com.biblioteca.entity.Emprestimo;
import com.biblioteca.entity.Livro;
import com.biblioteca.repository.AutorRepository;
import com.biblioteca.repository.EmprestimoRepository;
import com.biblioteca.repository.LivroRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import jakarta.annotation.security.RolesAllowed;

@ApplicationScoped
public class BibliotecaService {

    @Inject
    AutorRepository autorRepository;
    @Inject
    LivroRepository livroRepository;
    @Inject
    EmprestimoRepository emprestimoRepository;

    public List<Autor> listarTodosAutores() {
        return autorRepository.findAll();
    }

    public List<Livro> listarTodosLivros() {
        return livroRepository.findAll();
    }

    public List<Emprestimo> listarEmprestimosAtivos() {
        return emprestimoRepository.findAtivos();
    }

    public long contarTotalLivros() {
        return livroRepository.count();
    }

    public long contarLivrosDisponiveis() {
        return livroRepository.countByDisponivel(true);
    }

    public long contarEmprestimosAtivos() {
        return emprestimoRepository.countAtivos();
    }

    public long contarTotalAutores() {
        return autorRepository.count();
    }

    @Transactional
    public Autor criarAutor(String nome, String email) {
        Autor a = new Autor(nome, email);
        autorRepository.persist(a);
        return a;
    }

    @Transactional
    public Livro criarLivro(String titulo, String isbn, Long autorId) {
        Autor a = autorRepository.findById(autorId);
        if (a == null)
            throw new IllegalArgumentException("autor inexistente");
        Livro l = new Livro(titulo, isbn, a);
        livroRepository.persist(l);
        return l;
    }

    @Transactional
    public Emprestimo criarEmprestimo(Long livroId, String nomeUsuario, String emailUsuario, LocalDate previsao) {
        Livro l = livroRepository.findById(livroId);
        if (l == null)
            throw new IllegalArgumentException("livro inexistente");
        l.setDisponivel(false);
        Emprestimo e = new Emprestimo(nomeUsuario, emailUsuario, l);
        e.setDataDevolucaoPrevista(previsao);
        emprestimoRepository.persist(e);
        return e;
    }

    @Transactional
    public void devolverEmprestimo(Long emprestimoId) {
        Emprestimo e = emprestimoRepository.findById(emprestimoId);
        if (e == null)
            return;
        if (e.getDataDevolucao() == null) {
            e.setDataDevolucao(LocalDate.now());
            Livro l = e.getLivro();
            l.setDisponivel(true);
        }
    }

    @Transactional
    public void excluirLivro(Long livroId) {
        Livro l = livroRepository.findById(livroId);
        if (l == null)
            return;
        boolean temAtivo = l.getEmprestimos().stream().anyMatch(emp -> emp.getDataDevolucao() == null);
        if (temAtivo)
            throw new IllegalStateException("livro com empréstimo ativo");
        livroRepository.remove(l);
    }

    @Transactional
    public void excluirAutor(Long autorId) {
        Autor a = autorRepository.findById(autorId);
        if (a == null)
            return;

        if (!a.getLivros().isEmpty())
            throw new IllegalStateException("autor com livros");

        autorRepository.remove(a);
    }

    @RolesAllowed("ADMIN")
    @Transactional
    public void cadastrarLivro(String titulo, String isbn, LocalDate dataPublicacao,
            Integer numeroPaginas, Long autorId) {
        Autor autor = autorRepository.findById(autorId);
        if (autor == null) {
            throw new IllegalStateException("autor inexistente");
        }

        Livro livro = new Livro();
        livro.setTitulo(titulo);
        livro.setIsbn(isbn);
        livro.setDataPublicacao(dataPublicacao);
        livro.setNumeroPaginas(numeroPaginas);
        livro.setDisponivel(true);
        livro.setAutor(autor);

        livroRepository.persist(livro);
    }
}