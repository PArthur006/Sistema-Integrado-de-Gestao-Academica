package aluno;

import java.util.List;

public class AlunoNormal extends Aluno {
    public AlunoNormal(String nome, String matricula, String curso) {
        super(nome, matricula, curso);
    }

    @Override
    public boolean podeMatricular(String codigoDisciplina, List<String> preRequisitos) {
        return disciplinasMatriculadas.contains(codigoDisciplina) == false &&
               disciplinasConcluidas.containsAll(preRequisitos);
    }

    public boolean recebeNotas() {
        return true;
    }
}
