package avaliacao;

public class Nota {
    private String turma;
    private String aluno;
    private double[] notas;

    // Construtor para persistência
    public Nota(String turma, String aluno, double[] notas) {
        this.turma = turma;
        this.aluno = aluno;
        this.notas = notas;
    }

    // Construtor antigo para uso interno (mantido para compatibilidade)
    public Nota(double p1, double p2, double p3, double l, double s) {
        this.notas = new double[]{p1, p2, p3, l, s};
    }

    public String getTurma() {
        return turma;
    }

    public String getAluno() {
        return aluno;
    }

    public double[] getNotas() {
        return notas;
    }

    public double getP1() {
        return notas.length > 0 ? notas[0] : 0.0;
    }
    public double getP2() {
        return notas.length > 1 ? notas[1] : 0.0;
    }
    public double getP3() {
        return notas.length > 2 ? notas[2] : 0.0;
    }
    public double getL() {
        return notas.length > 3 ? notas[3] : 0.0;
    }
    public double getS() {
        return notas.length > 4 ? notas[4] : 0.0;
    }
}