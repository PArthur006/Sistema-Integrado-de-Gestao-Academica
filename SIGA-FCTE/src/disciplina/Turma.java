package disciplina;

import aluno.Aluno;
import avaliacao.Avaliacao;
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
    private List<Aluno> alunosMatriculados;
    private Avaliacao avaliacao;

    public Turma(String codigo, Disciplina disciplina, String professor, String semestre, String sala, String horario, int capacidade, String tipoAvaliacao) {
        this.codigo = codigo;
        this.disciplina = disciplina;
        this.professor = professor;
        this.semestre = semestre;
        this.sala = sala;
        this.horario = horario;
        this.capacidade = capacidade;
        this.tipoAvaliacao = tipoAvaliacao;
        this.alunosMatriculados = new ArrayList<>();
    }

    public boolean matricularAluno(Aluno aluno) {
        if (alunosMatriculados.stream().anyMatch(a -> a.getMatricula().equals(aluno.getMatricula()))) {
            return false;
        }
        
        if (alunosMatriculados.size() < capacidade && aluno.podeMatricular(disciplina)) {
            alunosMatriculados.add(aluno);
            aluno.getDisciplinasMatriculadas().add(disciplina);
            return true;
        }
        return false;
    }

    public String getCodigo() {
        return codigo;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public String getProfessor() {
        return professor;
    }

    public String getSemestre() {
        return semestre;
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

    public String getTipoAvaliacao() {
        return tipoAvaliacao;
    }

    public List<Aluno> getAlunosMatriculados() {
        return alunosMatriculados;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public void setTipoAvaliacao(String tipoAvaliacao) {
        this.tipoAvaliacao = tipoAvaliacao;
    }
}