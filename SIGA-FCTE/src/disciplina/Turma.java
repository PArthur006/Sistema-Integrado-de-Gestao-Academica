package disciplina;

import aluno.Aluno;
import java.util.ArrayList;
import java.util.List;

public class Turma {
    private String codigo;
    private Disciplina disciplina;
    private String professor;
    private String semestre;
    private String sala;
    private String horario;
    private int capacidade;
    private String tipoAvaliacao;
    private String modalidade;
    private List<Aluno> alunosMatriculados;

    public Turma(String codigo, Disciplina disciplina, String professor, String semestre, String sala, String horario, int capacidade, String tipoAvaliacao, String modalidade) {
        this.codigo = codigo;
        this.disciplina = disciplina;
        this.professor = professor;
        this.semestre = semestre;
        this.sala = sala;
        this.horario = horario;
        this.capacidade = capacidade;
        this.tipoAvaliacao = tipoAvaliacao;
        this.modalidade = modalidade;
        this.alunosMatriculados = new ArrayList<>();
    }

    public String getCodigo() { return codigo; }
    public Disciplina getDisciplina() { return disciplina; }
    public String getProfessor() { return professor; }
    public String getSemestre() { return semestre; }
    public String getSala() { return sala; }
    public String getHorario() { return horario; }
    public int getCapacidade() { return capacidade; }
    public String getTipoAvaliacao() { return tipoAvaliacao; }
    public String getModalidade() { return modalidade; }
    public List<Aluno> getAlunosMatriculados() { return alunosMatriculados; }

    public void setProfessor(String professor) { this.professor = professor; }
    public void setSemestre(String semestre) { this.semestre = semestre; }
    public void setSala(String sala) { this.sala = sala; }
    public void setHorario(String horario) { this.horario = horario; }
    public void setCapacidade(int capacidade) { this.capacidade = capacidade; }
    public void setTipoAvaliacao(String tipoAvaliacao) { this.tipoAvaliacao = tipoAvaliacao; }
    public void setModalidade(String modalidade) { this.modalidade = modalidade; }

    public boolean matricularAluno(Aluno aluno) {
        if (alunosMatriculados.size() < capacidade) {
            if (alunosMatriculados.stream().noneMatch(a -> a.getMatricula().equals(aluno.getMatricula()))) {
                alunosMatriculados.add(aluno);
                return true;
            }
        }
        return false;
    }

    public void desmatricularAluno(String matricula) {
        alunosMatriculados.removeIf(aluno -> aluno.getMatricula().equals(matricula));
    }
}