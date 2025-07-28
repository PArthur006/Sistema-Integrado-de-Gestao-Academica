package persistencia;

import aluno.Aluno;
import java.util.List;
import java.util.ArrayList;

public class AlunoRepository {
    private static AlunoRepository instance;
    private List<Aluno> alunos;

    private AlunoRepository() {
        alunos = ArquivoAluno.carregarAlunos();
    }

    public static AlunoRepository getInstance() {
        if (instance == null) {
            instance = new AlunoRepository();
        }
        return instance;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void adicionarAluno(Aluno aluno) {
        alunos.add(aluno);
        ArquivoAluno.salvarAlunos(alunos);
    }

    public void removerAluno(int idx) {
        alunos.remove(idx);
        ArquivoAluno.salvarAlunos(alunos);
    }
}
