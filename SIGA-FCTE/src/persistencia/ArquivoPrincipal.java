package persistencia;




public class ArquivoPrincipal {
    public static void salvarTudo() {
        ArquivoAluno.salvarAlunos(AlunoRepository.getInstance().getAlunos());
        // Salvamento de disciplinas e turmas é feito pela interface gráfica
    }
}