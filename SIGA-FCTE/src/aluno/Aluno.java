package aluno;

import java.util.ArrayList;
import java.util.List;

public abstract class Aluno {
    protected String nome;
    protected String matricula;
    protected String curso;
    protected List<String> disciplinasConcluidas;

    public Aluno(String nome, String matricula, String curso) {
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
        if (!disciplinasConcluidas.contains(codigoDisciplina)) {
            disciplinasConcluidas.add(codigoDisciplina);
        }
    }

    public abstract String getTipo();
}