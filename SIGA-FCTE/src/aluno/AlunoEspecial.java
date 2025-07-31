package aluno;

public class AlunoEspecial extends Aluno {
    public AlunoEspecial(String nome, String matricula, String curso) {
        super(nome, matricula, curso);
    }

    @Override
    public String getTipo() {
        return "Especial";
    }
}