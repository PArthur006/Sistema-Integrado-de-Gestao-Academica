package aluno;

import disciplina.Disciplina;

public class AlunoEspecial extends Aluno {
    public AlunoEspecial(String nome, String matricula, String curso) {
        super(nome, matricula, curso);
    }

    @Override
    public boolean podeMatricular(Disciplina disciplina) {
        return disciplinasMatriculadas.size() < 2;
    }

    @Override
    public String getTipo() {
        return "Especial";
    }
}