package persistencia;

/**
 * Classe utilitária para centralizar o salvamento de todos os dados da aplicação.
 */
public class ArquivoPrincipal {
    public static void salvarTudo() {
        /**
         * Invoca os métodos de salvamento de todos os repositórios.
         */
        ArquivoAluno.salvarAlunos(AlunoRepository.getInstance().getAlunos());
        ArquivoDisciplina.salvarDisciplinas(DisciplinaRepository.getInstance().getDisciplinas());
        ArquivoDisciplina.salvarTurmas(TurmaRepository.getInstance().getTurmas());
    }
}