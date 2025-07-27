package avaliacao;

import disciplina.Turma;
import disciplina.Disciplina;
import avaliacao.Nota;
import avaliacao.Frequencia;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Relatorio {
    // Relatório detalhado por turma
    public static void gerarPorTurma(Turma turma, List<Nota> notas, List<Frequencia> frequencias) {
        System.out.println("Relatório da Turma: " + turma.getCodigo());
        System.out.println("Disciplina: " + turma.getDisciplina().getNome());
        System.out.println("Professor: " + turma.getProfessor());
        System.out.println("Alunos Matriculados: " + turma.getAlunosMatriculados().size());
        System.out.println("\nAlunos:");
        for (aluno.Aluno aluno : turma.getAlunosMatriculados()) {
            System.out.print("- " + aluno.getNome() + " (" + aluno.getMatricula() + ")");
            Nota n = notas.stream().filter(x -> x.getTurma().equals(turma.getCodigo()) && x.getAluno().equals(aluno.getMatricula())).findFirst().orElse(null);
            Frequencia f = frequencias.stream().filter(x -> x.getTurma().equals(turma.getCodigo()) && x.getAluno().equals(aluno.getMatricula())).findFirst().orElse(null);
            if (n != null) {
                System.out.print(" | Notas: ");
                double[] ns = n.getNotas();
                for (int i = 0; i < ns.length; i++) {
                    System.out.print(ns[i]);
                    if (i < ns.length - 1) System.out.print(", ");
                }
            }
            if (f != null) {
                System.out.print(" | Faltas: " + f.getFaltas());
            }
            System.out.println();
        }
    }

    // Relatório por disciplina
    public static void gerarPorDisciplina(List<Turma> turmas, String codDisciplina) {
        System.out.println("Relatório da Disciplina: " + codDisciplina);
        turmas.stream()
            .filter(t -> t.getDisciplina().getCodigo().equals(codDisciplina))
            .forEach(t -> System.out.println("Turma: " + t.getCodigo() + " | Professor: " + t.getProfessor()));
    }

    // Relatório por professor
    public static void gerarPorProfessor(List<Turma> turmas, String professor) {
        System.out.println("Relatório do Professor: " + professor);
        turmas.stream()
            .filter(t -> t.getProfessor().equals(professor))
            .forEach(t -> System.out.println(t.getDisciplina().getNome() + " - Turma: " + t.getCodigo()));
    }

    // Boletim simples: apenas médias
    public static void gerarBoletimSimples(String aluno, List<Nota> notas) {
        System.out.println("Boletim Simples do Aluno: " + aluno);
        for (Nota n : notas) {
            if (n.getAluno().equals(aluno)) {
                double media = 0;
                double[] ns = n.getNotas();
                for (double v : ns) media += v;
                media /= ns.length;
                System.out.println("Turma: " + n.getTurma() + " | Média: " + String.format("%.2f", media));
            }
        }
    }

    // Boletim completo: notas, médias, situação
    public static void gerarBoletimCompleto(String aluno, List<Nota> notas, List<Frequencia> frequencias) {
        System.out.println("Boletim Completo do Aluno: " + aluno);
        for (Nota n : notas) {
            if (n.getAluno().equals(aluno)) {
                double media = 0;
                double[] ns = n.getNotas();
                for (double v : ns) media += v;
                media /= ns.length;
                Frequencia f = frequencias.stream().filter(x -> x.getTurma().equals(n.getTurma()) && x.getAluno().equals(aluno)).findFirst().orElse(null);
                double freq = (f != null) ? (100.0 - (f.getFaltas() * 100.0 / 60.0)) : 100.0; // Exemplo: 60 aulas
                String situacao = (media >= 5.0 && freq >= 75.0) ? "Aprovado" : (media < 5.0 ? "Reprovado por Nota" : "Reprovado por Frequência");
                System.out.println("Turma: " + n.getTurma() + " | Notas: " + java.util.Arrays.toString(ns) + " | Média: " + String.format("%.2f", media) + " | Frequência: " + String.format("%.1f", freq) + "% | Situação: " + situacao);
            }
        }
    }
}