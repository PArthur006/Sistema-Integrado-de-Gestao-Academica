package menus;

import aluno.Aluno;
import aluno.AlunoEspecial;
import aluno.AlunoNormal;
import persistencia.ArquivoAluno;
import disciplina.Disciplina;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuAluno {
    private static List<Aluno> alunos = new ArrayList<>();

    public static void exibir(Scanner scanner) {
        alunos = ArquivoAluno.carregarAlunos();
        while (true) {
            System.out.println("\nMenu Aluno");
            System.out.println("1. Cadastrar Aluno");
            System.out.println("2. Editar Aluno");
            System.out.println("3. Excluir Aluno");
            System.out.println("4. Listar Alunos");
            System.out.println("5. Trancar Disciplina");
            System.out.println("6. Trancar Semestre");
            System.out.println("7. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = -1;
            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido!");
                continue;
            }

            switch (opcao) {
                case 1:
                    cadastrarAluno(scanner);
                    break;
                case 2:
                    editarAluno(scanner);
                    break;
                case 3:
                    excluirAluno(scanner);
                    break;
                case 4:
                    listarAlunos();
                    break;
                case 5:
                    trancarDisciplina(scanner);
                    break;
                case 6:
                    trancarSemestre(scanner);
                    break;
                case 7:
                    ArquivoAluno.salvarAlunos(alunos);
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void cadastrarAluno(Scanner scanner) {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine();
        System.out.print("Curso: ");
        String curso = scanner.nextLine();
        int tipo = 0;
        while (tipo != 1 && tipo != 2) {
            System.out.print("Tipo (1-Normal / 2-Especial): ");
            try {
                tipo = Integer.parseInt(scanner.nextLine());
                if (tipo != 1 && tipo != 2) {
                    System.out.println("Opção inválida! Digite 1 ou 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido!");
            }
        }
        // Verifica duplicidade de matrícula
        if (alunos.stream().anyMatch(a -> a.getMatricula().equals(matricula))) {
            System.out.println("Já existe um aluno com essa matrícula!");
            return;
        }
        Aluno aluno;
        if (tipo == 1) {
            aluno = new AlunoNormal(nome, matricula, curso);
        } else {
            aluno = new AlunoEspecial(nome, matricula, curso);
        }
        alunos.add(aluno);
        ArquivoAluno.salvarAlunos(alunos);
        System.out.println("Aluno cadastrado com sucesso!");
    }

    private static void editarAluno(Scanner scanner) {
        System.out.print("Matrícula do aluno: ");
        String matricula = scanner.nextLine();
        Aluno aluno = alunos.stream().filter(a -> a.getMatricula().equals(matricula)).findFirst().orElse(null);
        if (aluno == null) {
            System.out.println("Aluno não encontrado!");
            return;
        }
        System.out.print("Novo nome: ");
        aluno.setNome(scanner.nextLine());
        System.out.print("Novo curso: ");
        aluno.setCurso(scanner.nextLine());
        ArquivoAluno.salvarAlunos(alunos);
        System.out.println("Aluno editado com sucesso!");
    }

    private static void excluirAluno(Scanner scanner) {
        System.out.print("Matrícula do aluno a ser excluído: ");
        String matricula = scanner.nextLine();
        if (alunos.removeIf(a -> a.getMatricula().equals(matricula))) {
            ArquivoAluno.salvarAlunos(alunos);
            System.out.println("Aluno removido com sucesso!");
        } else {
            System.out.println("Aluno não encontrado!");
        }
    }

    private static void listarAlunos() {
        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado.");
            return;
        }
        alunos.forEach(a -> System.out.println(
            a.getNome() + " - " + a.getMatricula() + " - " + a.getCurso() + " - " + a.getTipo()));
    }

    private static void trancarDisciplina(Scanner scanner) {
        System.out.print("Matrícula do aluno: ");
        String matricula = scanner.nextLine();
        Aluno aluno = alunos.stream().filter(a -> a.getMatricula().equals(matricula)).findFirst().orElse(null);
        if (aluno == null) {
            System.out.println("Aluno não encontrado!");
            return;
        }
        System.out.print("Código da disciplina a trancar: ");
        String codDisc = scanner.nextLine();
        Disciplina disc = aluno.getDisciplinasMatriculadas().stream().filter(d -> d.getCodigo().equals(codDisc)).findFirst().orElse(null);
        if (disc == null) {
            System.out.println("Disciplina não encontrada ou não está matriculado!");
            return;
        }
        aluno.trancarDisciplina(disc);
        System.out.println("Disciplina trancada!");
    }

    private static void trancarSemestre(Scanner scanner) {
        System.out.print("Matrícula do aluno: ");
        String matricula = scanner.nextLine();
        Aluno aluno = alunos.stream().filter(a -> a.getMatricula().equals(matricula)).findFirst().orElse(null);
        if (aluno == null) {
            System.out.println("Aluno não encontrado!");
            return;
        }
        aluno.trancarSemestre();
        System.out.println("Semestre trancado!");
    }

    public static List<Aluno> getAlunos() {
        return alunos;
    }
}
