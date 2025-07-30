package persistencia;

import disciplina.Turma;
import java.util.List;

public class TurmaRepository {
    private static TurmaRepository instance;
    private List<Turma> turmas;

    private TurmaRepository() {
        turmas = ArquivoDisciplina.carregarTurmas();
    }

    public static TurmaRepository getInstance() {
        if (instance == null) {
            instance = new TurmaRepository();
        }
        return instance;
    }

    public List<Turma> getTurmas() {
        return turmas;
    }
}