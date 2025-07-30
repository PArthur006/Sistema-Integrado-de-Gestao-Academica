package persistencia;

import disciplina.Disciplina;
import java.util.List;

public class DisciplinaRepository {
    private static DisciplinaRepository instance;
    private List<Disciplina> disciplinas;

    private DisciplinaRepository() {
        disciplinas = ArquivoDisciplina.carregarDisciplinas();
    }

    public static DisciplinaRepository getInstance() {
        if (instance == null) {
            instance = new DisciplinaRepository();
        }
        return instance;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }
}