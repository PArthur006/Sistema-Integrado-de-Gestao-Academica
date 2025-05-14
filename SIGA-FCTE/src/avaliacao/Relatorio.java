package avaliacao;

import java.util.List;
import turma.Turma;
import aluno.Aluno;

public class Relatorio {
    public static void gerarRelatorio(Turma turma){
        System.out.println("Relatório da Turma:");
        System.out.println("Disciplina: " + turma.getDisciplina().getNome());
        System.out.println("Professor: " + aluno.getNome() + " - Matricula: " + aluno.getMatricula());
    }
}
