package persistencia;




public class ArquivoPrincipal {
    public static void salvarTudo() {
        ArquivoAluno.salvarAlunos(AlunoRepository.getInstance().getAlunos());
        ArquivoDisciplina.salvarDisciplinas(DisciplinaRepository.getInstance().getDisciplinas());
        ArquivoDisciplina.salvarTurmas(TurmaRepository.getInstance().getTurmas());
    }
}