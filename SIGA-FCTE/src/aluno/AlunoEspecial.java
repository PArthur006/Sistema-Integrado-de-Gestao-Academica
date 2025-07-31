package aluno;

/**
 * Representa um aluno do tipo "Especial", com regras de negócio específicas.
 */
public class AlunoEspecial extends Aluno {
    public AlunoEspecial(String nome, String matricula, String curso) {
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
        return "Especial";
    }
}