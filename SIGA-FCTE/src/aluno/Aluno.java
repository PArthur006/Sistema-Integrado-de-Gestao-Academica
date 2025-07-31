package aluno;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstrata que representa um aluno no sistema.
 */
public abstract class Aluno {
    protected String nome;
    protected String matricula;
    protected String curso;
    protected List<String> disciplinasConcluidas;

    public Aluno(String nome, String matricula, String curso) {
        /**
         * Construtor
         */
        this.nome = nome;
        this.matricula = matricula;
        this.curso = curso;
        this.disciplinasConcluidas = new ArrayList<>();
    }

    public String getNome() { return nome; }

    public String getMatricula() { return matricula; }

    public String getCurso() { return curso; }

    public List<String> getDisciplinasConcluidas() { return disciplinasConcluidas; }

    public void setNome(String nome) { this.nome = nome; }

    public void setCurso(String curso) { this.curso = curso; }

    public void concluirDisciplina(String codigoDisciplina) {
        /**
         * Adiciona uma disciplina à lista de concluídas pelo aluno.
         */
        if (!disciplinasConcluidas.contains(codigoDisciplina)) {
            disciplinasConcluidas.add(codigoDisciplina);
        }
    }

    /**
     * Retorna o tipo do aluno ("Normal" ou "Especial").
     * Este método deve ser implementado pelas subclasses.
     */
    public abstract String getTipo();
}