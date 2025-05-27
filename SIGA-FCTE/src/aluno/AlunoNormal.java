package aluno;

import disciplina.Disciplina;

public class AlunoNormal extends Aluno {
    public AlunoNormal(String nome, String matricula, String curso) {
        super(nome, matricula, curso);
    }

    @Override
    public boolean podeMatricular(Disciplina disciplina) {
        return true;
    }

    @Override
    public String getTipo() {
        return "Normal";
    }
}