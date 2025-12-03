package com.biblioteca.controller;

import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.vertx.http.runtime.security.FormAuthenticationMechanism;
import jakarta.annotation.security.RolesAllowed;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;
import com.biblioteca.entity.Autor;
import com.biblioteca.entity.Emprestimo;
import com.biblioteca.entity.Livro;
import com.biblioteca.service.BibliotecaService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Named("bibliotecaBean")
@ViewScoped
public class BibliotecaBean implements Serializable {

    @Inject
    BibliotecaService service;

    @Inject
    SecurityIdentity securityIdentity;

    private List<Autor> autores;
    private List<Livro> livros;
    private List<Emprestimo> emprestimosAtivos;

    private long totalLivros;
    private long livrosDisponiveis;
    private long emprestimosAtivosCount;
    private long totalAutores;

    private String autorNome;
    private String autorEmail;

    private String livroTitulo;
    private String livroIsbn;
    private Long livroAutorId;

    private Long empLivroId;
    private String empNome;
    private String empEmail;
    private LocalDate empPrevista;

    @PostConstruct
    public void init() {
        carregarDados();
        carregarEstatisticas();
    }

    public void carregarDados() {
        try {
            autores = service.listarTodosAutores();
            livros = service.listarTodosLivros();
            emprestimosAtivos = service.listarEmprestimosAtivos();
        } catch (Exception e) {
            autores = Collections.emptyList();
            livros = Collections.emptyList();
            emprestimosAtivos = Collections.emptyList();
        }
    }

    public void carregarEstatisticas() {
        try {
            totalLivros = service.contarTotalLivros();
            livrosDisponiveis = service.contarLivrosDisponiveis();
            emprestimosAtivosCount = service.contarEmprestimosAtivos();
            totalAutores = service.contarTotalAutores();
        } catch (Exception e) {
            totalLivros = 0;
            livrosDisponiveis = 0;
            emprestimosAtivosCount = 0;
            totalAutores = 0;
        }
    }

    public void recarregarDados() {
        init();
    }

    public void salvarAutor() {
        service.criarAutor(autorNome, autorEmail);
        autorNome = null;
        autorEmail = null;
        recarregarDados();
    }

    @RolesAllowed("ADMIN")
    public void salvarLivro() {
        service.criarLivro(livroTitulo, livroIsbn, livroAutorId);
        livroTitulo = null;
        livroIsbn = null;
        livroAutorId = null;
        recarregarDados();
    }

    public void salvarEmprestimo() {
        if (empPrevista == null)
            empPrevista = LocalDate.now().plusDays(14);
        service.criarEmprestimo(empLivroId, empNome, empEmail, empPrevista);
        empLivroId = null;
        empNome = null;
        empEmail = null;
        empPrevista = null;
        recarregarDados();
    }

    public void devolver(Long emprestimoId) {
        service.devolverEmprestimo(emprestimoId);
        recarregarDados();
    }

    @RolesAllowed("ADMIN")
    public void excluirLivro(Long livroId) {
        service.excluirLivro(livroId);
        recarregarDados();
    }

    public void excluirAutor(Long autorId) {
        service.excluirAutor(autorId);
        recarregarDados();
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public List<Emprestimo> getEmprestimosAtivos() {
        return emprestimosAtivos;
    }

    public long getTotalLivros() {
        return totalLivros;
    }

    public long getLivrosDisponiveis() {
        return livrosDisponiveis;
    }

    public long getEmprestimosAtivosCount() {
        return emprestimosAtivosCount;
    }

    public long getTotalAutores() {
        return totalAutores;
    }

    public String getAutorNome() {
        return autorNome;
    }

    public void setAutorNome(String autorNome) {
        this.autorNome = autorNome;
    }

    public String getAutorEmail() {
        return autorEmail;
    }

    public void setAutorEmail(String autorEmail) {
        this.autorEmail = autorEmail;
    }

    public String getLivroTitulo() {
        return livroTitulo;
    }

    public void setLivroTitulo(String livroTitulo) {
        this.livroTitulo = livroTitulo;
    }

    public String getLivroIsbn() {
        return livroIsbn;
    }

    public void setLivroIsbn(String livroIsbn) {
        this.livroIsbn = livroIsbn;
    }

    public Long getLivroAutorId() {
        return livroAutorId;
    }

    public void setLivroAutorId(Long livroAutorId) {
        this.livroAutorId = livroAutorId;
    }

    public Long getEmpLivroId() {
        return empLivroId;
    }

    public void setEmpLivroId(Long empLivroId) {
        this.empLivroId = empLivroId;
    }

    public String getEmpNome() {
        return empNome;
    }

    public void setEmpNome(String empNome) {
        this.empNome = empNome;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public LocalDate getEmpPrevista() {
        return empPrevista;
    }

    public void setEmpPrevista(LocalDate empPrevista) {
        this.empPrevista = empPrevista;
    }

    public String logout() {
        if (securityIdentity != null && !securityIdentity.isAnonymous()) {
            FormAuthenticationMechanism.logout(securityIdentity);
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        facesContext.getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    public void emprestarLivro(Long livroId) {
        if (securityIdentity == null || securityIdentity.isAnonymous()) {
            return;
        }
        if (empPrevista == null) {
            empPrevista = LocalDate.now().plusDays(14);
        }
        String nome = securityIdentity.getPrincipal().getName();
        String email = securityIdentity.getPrincipal().getName();
        service.criarEmprestimo(livroId, nome, email, empPrevista);
        empLivroId = null;
        empNome = null;
        empEmail = null;
        empPrevista = null;
        recarregarDados();
    }
}