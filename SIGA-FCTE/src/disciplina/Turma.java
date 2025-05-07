package disciplina;

import aluno.Aluno;
import java.util.ArrayList;
import java.util.List;

public class Turma {
    private Disciplina disciplina;
    private String professor;
    private String semestre;
    private String formaAvaliacao; // "F1" ou "F2"
    private boolean presencial;
    private String sala;
    private String horario;
    private int capacidade;
    private List<Aluno> alunosMatriculados;
    
    // MÉTODO CONSTRUTOR
    public Turma(Disciplina disciplina, String professor, String semestre, String formaAvaliacao, boolean presencial,
            String sala, String horario, int capacidade) {
        this.disciplina = disciplina;
        this.professor = professor;
        this.semestre = semestre;
        this.formaAvaliacao = formaAvaliacao;
        this.presencial = presencial;
        this.sala = presencial ? sala : "Remota";
        this.horario = horario;
        this.capacidade = capacidade;
        this.alunosMatriculados = new ArrayList<>();
    }

    public boolean adicionarAluno(Aluno aluno){
        if (alunosMatriculados.size() < capacidade){
            alunosMatriculados.add(aluno);
            return true;
        }
        return false;
    }

    public void removerAluno(Aluno aluno){
        alunosMatriculados.remove(aluno);
    }

    // GETTERS

    public String getProfessor() {
        return professor;
    }

    public String getSemestre() {
        return semestre;
    }

    public String getFormaAvaliacao() {
        return formaAvaliacao;
    }

    public boolean isPresencial() {
        return presencial;
    }

    public String getSala() {
        return sala;
    }

    public String getHorario() {
        return horario;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public List<Aluno> getAlunosMatriculados() {
        return alunosMatriculados;
    }

    @Override
    public String toString(){
        return "Turma de " + disciplina.getNome() + " | Professor: " + professor + " | Semestre: " + semestre + " | Horário: " + horario + " | Modalidade: " + (presencial ? "Presecial, Sala " + sala : "Remota");
    }
}
