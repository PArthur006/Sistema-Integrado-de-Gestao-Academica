package menus;

import avaliacao.Avaliacao;
import avaliacao.Boletim;
import avaliacao.Frequencia;
import avaliacao.Nota;
import avaliacao.Relatorio;
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
        System.out.print("Código da turma: ");
        String codigoTurma = scanner.nextLine();
        
        Turma turma = turmas.stream()
            .filter(t -> t.getCodigo().equals(codigoTurma))
            .findFirst()
            .orElse(null);
            
        if (turma == null) {
            System.out.println("Turma não encontrada!");
            return;
        }
        
        Avaliacao avaliacao = turma.getAvaliacao();
        if (avaliacao == null) {
            avaliacao = new Avaliacao(turma);
            turma.setAvaliacao(avaliacao);
        }
        
        System.out.print("Matrícula do aluno: ");
        String matricula = scanner.nextLine();
        
        double p1 = lerNota(scanner, "Nota P1");
        double p2 = lerNota(scanner, "Nota P2");
        double p3 = lerNota(scanner, "Nota P3");
        double l = lerNota(scanner, "Nota L");
        double s = lerNota(scanner, "Nota S");
        
        Nota nota = new Nota(p1, p2, p3, l, s);
        avaliacao.lancarNota(matricula, nota);
        System.out.println("Notas lançadas com sucesso!");
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
        System.out.print("Código da turma: ");
        String codigoTurma = scanner.nextLine();
        
        Turma turma = turmas.stream()
            .filter(t -> t.getCodigo().equals(codigoTurma))
            .findFirst()
            .orElse(null);
            
        if (turma == null) {
            System.out.println("Turma não encontrada!");
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
        
        Frequencia frequencia = new Frequencia(totalAulas, presencas);
        avaliacao.lancarFrequencia(matricula, frequencia);
        System.out.println("Frequência lançada com sucesso!");
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
        System.out.println("2. Por Professor");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();
        
        if (opcao == 1) {
            System.out.print("Código da turma: ");
            String codigoTurma = scanner.nextLine();
            turmas.stream()
                .filter(t -> t.getCodigo().equals(codigoTurma))
                .findFirst()
                .ifPresent(Relatorio::gerarPorTurma);
        } else if (opcao == 2) {
            System.out.print("Nome do professor: ");
            String professor = scanner.nextLine();
            Relatorio.gerarPorProfessor(turmas, professor);
        }
    }
}