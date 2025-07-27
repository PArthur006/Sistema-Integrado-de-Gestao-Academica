package avaliacao;

public class Frequencia {
    private String turma;
    private String aluno;
    private int faltas;
    private int totalAulas;
    private int presencas;

    // Construtor para persistência
    public Frequencia(String turma, String aluno, int faltas) {
        this.turma = turma;
        this.aluno = aluno;
        this.faltas = faltas;
    }

    // Construtor antigo para uso interno (mantido para compatibilidade)
    public Frequencia(int totalAulas, int presencas) {
        this.totalAulas = totalAulas;
        this.presencas = presencas;
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

    public double calcularFrequencia() {
        if (totalAulas > 0) {
            return (double) presencas / totalAulas * 100;
        } else {
            return 0.0;
        }
    }
}