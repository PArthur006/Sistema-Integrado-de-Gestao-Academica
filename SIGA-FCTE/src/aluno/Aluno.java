package aluno;

import java.util.ArrayList;
import java.util.List;

public abstract class Aluno {
    protected String nome;
    protected String matricula;
    protected String curso;
    protected List<String> disciplinasMatriculadas;
    protected List<String> disciplinasConcluidas;

    public Aluno(String nome, String matricula, String curso){
        this.nome = nome;
        this.matricula = matricula;
        this.curso = curso;
        this.disciplinasMatriculadas = new ArrayList<>();
        this.disciplinasConcluidas = new ArrayList<>();
    }

    public abstract boolean podeMatricular(String codigoDisciplina, List<String> preRequisitos);

    public boolean matricular(String codigoDisciplina, List<String> preRequisitos){
        if (podeMatricular(codigoDisciplina, preRequisitos)) {
            disciplinasMatriculadas.add(codigoDisciplina);
            return true;
        }
        return false;
    }

    public void trancarDisciplina(String disciplina){
        disciplinasMatriculadas.remove(disciplina);
    }

    public void trancarSemestre(){
        disciplinasMatriculadas.clear();
    }

    public void adicionarDisciplinaConcluida(String codigoDisciplina) {
        if (!disciplinasConcluidas.contains(codigoDisciplina)) {
            disciplinasConcluidas.add(codigoDisciplina);
        }
    }

    public String getNome() {
        return nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public List<String> getDisciplinasMatriculadas() {
        return disciplinasMatriculadas;
    }

    public List<String> getDisciplinasConcluidas() {
        return disciplinasConcluidas;
    }

    @Override
    public String toString() {
        return nome + " | Matrícula: " + matricula;
    }
}
