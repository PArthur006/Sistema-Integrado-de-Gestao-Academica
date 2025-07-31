package avaliacao;

/**
 * Representa o registro de frequência de um aluno em uma turma.
 */
public class Frequencia {
    private String turma;
    private String aluno;
    private int faltas;

    public Frequencia(String turma, String aluno, int faltas) {
        /**
         * Construtor
         */
        this.turma = turma;
        this.aluno = aluno;
        this.faltas = faltas;
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

    public int getFaltas() {
        return faltas;
    }
}