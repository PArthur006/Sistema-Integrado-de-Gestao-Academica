package aluno;

import java.util.List;

public abstract class Aluno {
    protected String nome;
    protected String matricula;
    protected String curso;
    protected List<String> disciplinasMatriculadas;

    // MÉTODO CONSTRUTOR
    public Aluno(String nome, String matricula, String curso){
        this.nome = nome;
        this.matricula = matricula;
        this.curso = curso;
    }

    public String getMatricula(){
        return matricula;
    }

    public abstract boolean podeMatricular(String disciplina);

    public void matricular(String disciplina){
        if(podeMatricular(disciplina)){
            disciplinasMatriculadas.add(disciplina);
        }
    }

    public void trancarDisciplina(String disciplina){
        disciplinasMatriculadas.remove(disciplina);
    }

    public void trancarSemestre(){
        disciplinasMatriculadas.clear();
    }

    // GETTERS E SETTERS

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public List<String> getDisciplinasMatriculadas() {
        return disciplinasMatriculadas;
    }

}

