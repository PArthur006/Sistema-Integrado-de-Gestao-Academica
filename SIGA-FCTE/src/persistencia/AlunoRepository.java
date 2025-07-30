package persistencia;

import aluno.Aluno;
import disciplina.Turma;
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

    public void removerAluno(String matricula) {
        // Exclusão em cascata: remove o aluno de todas as turmas primeiro.
        List<Turma> todasAsTurmas = TurmaRepository.getInstance().getTurmas();
        for (Turma turma : todasAsTurmas) {
            turma.desmatricularAluno(matricula);
        }

        // Agora, remove o aluno do repositório principal.
        alunos.removeIf(aluno -> aluno.getMatricula().equals(matricula));

        // Salva as alterações nos dois arquivos para manter a consistência.
        ArquivoAluno.salvarAlunos(alunos);
        ArquivoDisciplina.salvarTurmas(todasAsTurmas);
    }
}
