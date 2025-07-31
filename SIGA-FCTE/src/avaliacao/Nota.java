package avaliacao;

/**
 * Representa o conjunto de notas de um aluno em uma turma.
 */
public class Nota {
    private String turma;
    private String aluno;
    private double[] notas;

    public Nota(String turma, String aluno, double[] notas) {
        /**
         * Construtor
         */
        this.turma = turma;
        this.aluno = aluno;
        this.notas = notas;
    }

    /**
     * Getters
     */
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