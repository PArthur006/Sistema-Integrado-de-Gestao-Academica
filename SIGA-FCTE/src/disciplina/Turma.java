package disciplina;

import aluno.Aluno;
import java.util.ArrayList;
import java.util.List;

public class Turma {
    private Disciplina disciplina;
    private String professor;
    private String semestre;
    private String formaAvaliacao;
    private boolean presencial;
    private String sala;
    private String horario;
    private int capacidade;
    private List<Aluno> alunosMatriculados;

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
        String codigoDisciplina = disciplina.getCodigo();
        List<String> preRequisitos = disciplina.getPreRequisitos();

        if (alunosMatriculados.size() < capacidade &&
            aluno.matricular(codigoDisciplina, preRequisitos)) {

            alunosMatriculados.add(aluno);
            return true;
        }

        return false;
    }

    public void removerAluno(Aluno aluno){
        alunosMatriculados.remove(aluno);
    }

    public List<Aluno> getAlunosMatriculados() {
        return alunosMatriculados;
    }

    @Override
    public String toString(){
        return "Turma de " + disciplina.getNome() +
               " | Professor: " + professor +
               " | Semestre: " + semestre +
               " | Horário: " + horario +
               " | Modalidade: " + (presencial ? "Presencial (Sala " + sala + ")" : "Remota");
    }
}
