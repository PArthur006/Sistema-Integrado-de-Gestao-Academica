package avaliacao;

public class Frequencia {
    private String turma;
    private String aluno;
    private int faltas;

    public Frequencia(String turma, String aluno, int faltas) {
        this.turma = turma;
        this.aluno = aluno;
        this.faltas = faltas;
    }

    public String getTurma() {
        return turma;
    }

    public String getAluno() {
        return aluno;
    }

    public int getFaltas() {
        return faltas;
    }
}