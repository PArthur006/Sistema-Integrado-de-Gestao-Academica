package persistencia;

import menus.MenuAluno;
import menus.MenuDisciplina;

public class ArquivoPrincipal {
    public static void salvarTudo() {
        ArquivoAluno.salvarAlunos(MenuAluno.getAlunos());
        ArquivoDisciplina.salvarDisciplinas(MenuDisciplina.getDisciplinas());
        ArquivoDisciplina.salvarTurmas(MenuDisciplina.getTurmas());
    }
}