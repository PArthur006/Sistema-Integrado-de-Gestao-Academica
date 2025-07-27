package aluno;

import disciplina.Disciplina;
import java.util.ArrayList;
import java.util.List;

public abstract class Aluno {
    protected String nome;
    protected String matricula;
    protected String curso;
    protected List<Disciplina> disciplinasMatriculadas;
    protected List<Disciplina> disciplinasTrancadas = new ArrayList<>();
    protected boolean semestreTrancado = false;

    public Aluno(String nome, String matricula, String curso) {
        this.nome = nome;
        this.matricula = matricula;
        this.curso = curso;
        this.disciplinasMatriculadas = new ArrayList<>();
    }
    public void trancarDisciplina(Disciplina disciplina) {
        if (disciplinasMatriculadas.contains(disciplina) && !disciplinasTrancadas.contains(disciplina)) {
            disciplinasTrancadas.add(disciplina);
            disciplinasMatriculadas.remove(disciplina);
        }
    }

    public void destrancarDisciplina(Disciplina disciplina) {
        disciplinasTrancadas.remove(disciplina);
    }

    public boolean isDisciplinaTrancada(Disciplina disciplina) {
        return disciplinasTrancadas.contains(disciplina);
    }

    public void trancarSemestre() {
        semestreTrancado = true;
        disciplinasTrancadas.addAll(disciplinasMatriculadas);
        disciplinasMatriculadas.clear();
    }

    public void destrancarSemestre() {
        semestreTrancado = false;
    }

    public boolean isSemestreTrancado() {
        return semestreTrancado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public List<Disciplina> getDisciplinasMatriculadas() {
        return disciplinasMatriculadas;
    }

    public List<Disciplina> getDisciplinasTrancadas() {
        return disciplinasTrancadas;
    }

    public abstract boolean podeMatricular(Disciplina disciplina);
    public abstract String getTipo();
}
