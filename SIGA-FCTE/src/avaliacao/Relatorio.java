package avaliacao;

import disciplina.Turma;
import java.util.List;

public class Relatorio {
    public static void gerarPorTurma(Turma turma) {
        System.out.println("Relatório da Turma: " + turma.getCodigo());
        System.out.println("Disciplina: " + turma.getDisciplina().getNome());
        System.out.println("Professor: " + turma.getProfessor());
        System.out.println("Alunos Matriculados: " + turma.getAlunosMatriculados().size());
    }

    public static void gerarPorProfessor(List<Turma> turmas, String professor) {
        System.out.println("Relatório do Professor: " + professor);
        turmas.stream()
            .filter(t -> t.getProfessor().equals(professor))
            .forEach(t -> System.out.println(t.getDisciplina().getNome() + " - " + t.getCodigo()));
    }
}