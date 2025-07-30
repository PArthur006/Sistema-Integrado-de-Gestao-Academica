package menus;

import avaliacao.Avaliacao;
import avaliacao.Boletim;
import avaliacao.Frequencia;
import avaliacao.Nota;
import avaliacao.Relatorio;
import persistencia.ArquivoAvaliacao;
import aluno.Aluno;
import aluno.AlunoEspecial;
import disciplina.Turma;
import java.util.List;
import java.util.Scanner;

public class MenuAvaliacao {
    public static void exibir(Scanner scanner) {
        List<Turma> turmas = MenuDisciplina.getTurmas();
        
        while (true) {
            System.out.println("\nMenu Avaliação");
            System.out.println("1. Lançar Notas");
            System.out.println("2. Lançar Frequências");
            System.out.println("3. Gerar Boletim");
            System.out.println("4. Gerar Relatório");
            System.out.println("5. Voltar");
            System.out.print("Escolha uma opção: ");
            
            int opcao = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcao) {
                case 1:
                    lancarNotas(scanner, turmas);
                    break;
                case 2:
                    lancarFrequencias(scanner, turmas);
                    break;
                case 3:
                    gerarBoletim(scanner, turmas);
                    break;
                case 4:
                    gerarRelatorio(scanner, turmas);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void lancarNotas(Scanner scanner, List<Turma> turmas) {
        Turma turma = encontrarTurma(scanner, turmas);
        if (turma == null) {
            return;
        }
        
        Avaliacao avaliacao = turma.getAvaliacao();
        if (avaliacao == null) {
            avaliacao = new Avaliacao(turma);
            turma.setAvaliacao(avaliacao);
        }
        
        System.out.print("Matrícula do aluno: ");
        String matricula = scanner.nextLine();

        // Impedir lançamento para AlunoEspecial acima do limite
        Aluno aluno = turma.getAlunosMatriculados().stream().filter(a -> a.getMatricula().equals(matricula)).findFirst().orElse(null);
        if (aluno instanceof AlunoEspecial && aluno.getDisciplinasMatriculadas().size() >= 2) {
            System.out.println("Aluno Especial não pode receber notas em mais de 2 disciplinas!");
            return;
        }
        
        double p1 = lerNota(scanner, "Nota P1");
        double p2 = lerNota(scanner, "Nota P2");
        double p3 = lerNota(scanner, "Nota P3");
        double l = lerNota(scanner, "Nota L");
        double s = lerNota(scanner, "Nota S");
        Nota nota = new Nota(turma.getCodigo(), matricula, new double[]{p1, p2, p3, l, s});
        avaliacao.lancarNota(matricula, nota);
        // Persistir
        List<Nota> todasNotas = ArquivoAvaliacao.lerAvaliacoes();
        todasNotas.removeIf(n -> n.getTurma().equals(turma.getCodigo()) && n.getAluno().equals(matricula));
        todasNotas.add(nota);
        ArquivoAvaliacao.salvarAvaliacoes(todasNotas);
        System.out.println("Notas lançadas e salvas com sucesso!");
    }

    private static double lerNota(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem + ": ");
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido!");
            }
        }
    }

    private static void lancarFrequencias(Scanner scanner, List<Turma> turmas) {
        Turma turma = encontrarTurma(scanner, turmas);
        if (turma == null) {
            return;
        }
        
        Avaliacao avaliacao = turma.getAvaliacao();
        if (avaliacao == null) {
            avaliacao = new Avaliacao(turma);
            turma.setAvaliacao(avaliacao);
        }
        
        System.out.print("Matrícula do aluno: ");
        String matricula = scanner.nextLine();
        System.out.print("Total de aulas: ");
        int totalAulas = scanner.nextInt();
        System.out.print("Presenças: ");
        int presencas = scanner.nextInt();
        scanner.nextLine();
        
        int faltas = totalAulas - presencas;
        Frequencia frequencia = new Frequencia(turma.getCodigo(), matricula, faltas);
        avaliacao.lancarFrequencia(matricula, frequencia);
        // Persistir
        List<Frequencia> todasFrequencias = ArquivoAvaliacao.lerFrequencias();
        todasFrequencias.removeIf(f -> f.getTurma().equals(turma.getCodigo()) && f.getAluno().equals(matricula));
        todasFrequencias.add(frequencia);
        ArquivoAvaliacao.salvarFrequencias(todasFrequencias);
        System.out.println("Frequência lançada e salva com sucesso!");
    }

    private static void gerarBoletim(Scanner scanner, List<Turma> turmas) {
        System.out.print("Matrícula do aluno: ");
        String matricula = scanner.nextLine();
        
        for (Turma turma : turmas) {
            Avaliacao avaliacao = turma.getAvaliacao();
            if (avaliacao != null) {
                Boletim boletim = avaliacao.gerarBoletim(matricula);
                if (boletim != null) {
                    System.out.println("Disciplina: " + turma.getDisciplina().getNome());
                    System.out.println("Média: " + boletim.getMediaFinal());
                    System.out.println("Situação: " + (boletim.isAprovado() ? "Aprovado" : "Reprovado"));
                }
            }
        }
    }

    private static void gerarRelatorio(Scanner scanner, List<Turma> turmas) {
        System.out.println("1. Por Turma");
        System.out.println("2. Por Disciplina");
        System.out.println("3. Por Professor");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();
        List<Nota> notas = ArquivoAvaliacao.lerAvaliacoes();
        List<Frequencia> frequencias = ArquivoAvaliacao.lerFrequencias();
        if (opcao == 1) {
            System.out.print("Código da turma: ");
            String codigoTurma = scanner.nextLine();
            turmas.stream()
                .filter(t -> t.getCodigo().equals(codigoTurma))
                .findFirst()
                .ifPresent(t -> Relatorio.gerarPorTurma(t, notas, frequencias));
        } else if (opcao == 2) {
            System.out.print("Código da disciplina: ");
            String codDisc = scanner.nextLine();
            Relatorio.gerarPorDisciplina(turmas, codDisc);
        } else if (opcao == 3) {
            System.out.print("Nome do professor: ");
            String professor = scanner.nextLine();
            Relatorio.gerarPorProfessor(turmas, professor);
        }
    }

    private static Turma encontrarTurma(Scanner scanner, List<Turma> turmas) {
        System.out.print("Código da turma: ");
        String codigoTurma = scanner.nextLine();

        Turma turma = turmas.stream()
                .filter(t -> t.getCodigo().equals(codigoTurma))
                .findFirst()
                .orElse(null);

        if (turma == null) {
            System.out.println("Turma não encontrada!");
        }
        return turma;
    }
}