package aluno;

import java.util.List;

public class AlunoEspecial extends Aluno {
    public AlunoEspecial(String nome, String matricula, String curso) {
        super(nome, matricula, curso);
    }

    @Override
    public boolean podeMatricular(String codigoDisciplina, List<String> preRequisitos) {
        return disciplinasMatriculadas.size() < 1;
    }

    public boolean recebeNotas() {
        return false;
    }
}
