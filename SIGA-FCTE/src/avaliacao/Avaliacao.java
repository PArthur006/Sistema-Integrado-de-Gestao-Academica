package avaliacao;

import disciplina.Turma;
import java.util.HashMap;
import java.util.Map;

public class Avaliacao {
    private Turma turma;
    private Map<String, Nota> notasAlunos;
    private Map<String, Frequencia> frequenciasAlunos;

    public Avaliacao(Turma turma) {
        this.turma = turma;
        this.notasAlunos = new HashMap<>();
        this.frequenciasAlunos = new HashMap<>();
    }

    public void lancarNota(String matricula, Nota nota) {
        notasAlunos.put(matricula, nota);
    }

    public void lancarFrequencia(String matricula, Frequencia frequencia) {
        frequenciasAlunos.put(matricula, frequencia);
    }

    public Boletim gerarBoletim(String matricula) {
        Nota nota = notasAlunos.get(matricula);
        Frequencia frequencia = frequenciasAlunos.get(matricula);
        if (nota != null && frequencia != null) {
            return new Boletim(turma, nota, frequencia);
        }
        return null;
    }
}