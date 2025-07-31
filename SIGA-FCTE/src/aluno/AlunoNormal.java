package aluno;

/**
 * Representa um aluno do tipo "Normal".
 */
public class AlunoNormal extends Aluno {
    public AlunoNormal(String nome, String matricula, String curso) {
        /**
         * Construtor
         */
        super(nome, matricula, curso);
    }

    @Override
    public String getTipo() {
        /**
         * Retorna o tipo do aluno.
         */
        return "Normal";
    }
}