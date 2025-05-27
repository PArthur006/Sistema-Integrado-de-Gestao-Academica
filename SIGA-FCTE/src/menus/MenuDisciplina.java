package menus;

import disciplina.Disciplina;
import disciplina.Turma;
import persistencia.ArquivoDisciplina;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import aluno.Aluno;

public class MenuDisciplina {
    private static List<Disciplina> disciplinas = new ArrayList<>();
    private static List<Turma> turmas = new ArrayList<>();

    public static void exibir(Scanner scanner) {
        
        disciplinas = ArquivoDisciplina.carregarDisciplinas();
        turmas = ArquivoDisciplina.carregarTurmas();
        
        while (true) {
            System.out.println("\nMenu Disciplina");
            System.out.println("1. Cadastrar Disciplina");
            System.out.println("2. Editar Disciplina");
            System.out.println("3. Criar Turma");
            System.out.println("4. Editar Turma");
            System.out.println("5. Matricular Aluno");
            System.out.println("6. Desmatricular Aluno");
            System.out.println("7. Listar Turmas");
            System.out.println("8. Voltar");
            System.out.print("Escolha uma opção: ");
            
            int opcao = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcao) {
                case 1:
                    cadastrarDisciplina(scanner);
                    break;
                case 2:
                    editarDisciplina(scanner);
                    break;
                case 3:
                    criarTurma(scanner);
                    break;
                case 4:
                    editarTurma(scanner);
                    break;
                case 5:
                    matricularAluno(scanner);
                    break;
                case 6:
                    desmatricularAluno(scanner);
                    break;
                case 7:
                    listarTurmas();
                    break;
                case 8:
                    ArquivoDisciplina.salvarDisciplinas(disciplinas);
                    ArquivoDisciplina.salvarTurmas(turmas);
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void cadastrarDisciplina(Scanner scanner) {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        
        System.out.print("Código: ");
        String codigo = scanner.nextLine();
        
        int cargaHoraria = 0;
        while (cargaHoraria <= 0) {
            System.out.print("Carga horária: ");
            try {
                cargaHoraria = Integer.parseInt(scanner.nextLine());
                if (cargaHoraria <= 0) {
                    System.out.println("A carga horária deve ser maior que zero!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido!");
            }
        }
        
        Disciplina disciplina = new Disciplina(nome, codigo, cargaHoraria);
        disciplinas.add(disciplina);
        System.out.println("Disciplina cadastrada com sucesso!");
    }

    private static void editarDisciplina(Scanner scanner) {
        System.out.print("Código da disciplina: ");
        String codigo = scanner.nextLine();
        
        Disciplina disciplina = disciplinas.stream()
            .filter(d -> d.getCodigo().equals(codigo))
            .findFirst()
            .orElse(null);
            
        if (disciplina == null) {
            System.out.println("Disciplina não encontrada!");
            return;
        }
        
        System.out.print("Novo nome (" + disciplina.getNome() + "): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.isEmpty()) {
            disciplina.setNome(novoNome);
        }
        
        System.out.print("Nova carga horária (" + disciplina.getCargaHoraria() + "): ");
        String novaCarga = scanner.nextLine();
        if (!novaCarga.isEmpty()) {
            disciplina.setCargaHoraria(Integer.parseInt(novaCarga));
        }
        
        System.out.println("Disciplina atualizada com sucesso!");
        ArquivoDisciplina.salvarDisciplinas(disciplinas);
    }

    private static void criarTurma(Scanner scanner) {
        System.out.print("Código da disciplina: ");
        String codigoDisciplina = scanner.nextLine();
        
        Disciplina disciplina = disciplinas.stream()
            .filter(d -> d.getCodigo().equals(codigoDisciplina))
            .findFirst()
            .orElse(null);
            
        if (disciplina == null) {
            System.out.println("Disciplina não encontrada!");
            return;
        }
        
        System.out.print("Código da turma: ");
        String codigoTurma = scanner.nextLine();
        System.out.print("Professor: ");
        String professor = scanner.nextLine();
        System.out.print("Semestre: ");
        String semestre = scanner.nextLine();
        System.out.print("Sala: ");
        String sala = scanner.nextLine();
        System.out.print("Horário: ");
        String horario = scanner.nextLine();
        System.out.print("Capacidade: ");
        int capacidade = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Tipo de avaliação (Simples/Ponderada): ");
        String tipoAvaliacao = scanner.nextLine();
        
        Turma turma = new Turma(codigoTurma, disciplina, professor, semestre, sala, horario, capacidade, tipoAvaliacao);
        turmas.add(turma);
        System.out.println("Turma criada com sucesso!");
    }

    private static void editarTurma(Scanner scanner) {
        System.out.print("Código da turma: ");
        String codigo = scanner.nextLine();
        
        Turma turma = turmas.stream()
            .filter(t -> t.getCodigo().equals(codigo))
            .findFirst()
            .orElse(null);
            
        if (turma == null) {
            System.out.println("Turma não encontrada!");
            return;
        }
        
        System.out.print("Novo professor (" + turma.getProfessor() + "): ");
        String novoProfessor = scanner.nextLine();
        if (!novoProfessor.isEmpty()) {
            turma.setProfessor(novoProfessor);
        }
        
        System.out.print("Nova sala (" + turma.getSala() + "): ");
        String novaSala = scanner.nextLine();
        if (!novaSala.isEmpty()) {
            turma.setSala(novaSala);
        }
        
        System.out.println("Turma atualizada com sucesso!");
        ArquivoDisciplina.salvarTurmas(turmas);
    }

    private static void matricularAluno(Scanner scanner) {
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
        
        System.out.print("Matrícula do aluno: ");
        String matricula = scanner.nextLine();
        
        Aluno aluno = MenuAluno.getAlunos().stream()
            .filter(a -> a.getMatricula().equals(matricula))
            .findFirst()
            .orElse(null);
            
        if (aluno == null) {
            System.out.println("Aluno não encontrado!");
            return;
        }
        
        if (turma.matricularAluno(aluno)) {
            System.out.println("Aluno matriculado com sucesso!");
            ArquivoDisciplina.salvarTurmas(turmas);
        } else {
            System.out.println("Falha ao matricular aluno (turma lotada ou aluno já matriculado)");
        }
    }

    private static void desmatricularAluno(Scanner scanner) {
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
        
        System.out.print("Matrícula do aluno: ");
        String matricula = scanner.nextLine();
        
        if (turma.getAlunosMatriculados().removeIf(a -> a.getMatricula().equals(matricula))) {
            System.out.println("Aluno desmatriculado com sucesso!");
            ArquivoDisciplina.salvarTurmas(turmas);
        } else {
            System.out.println("Aluno não encontrado na turma!");
        }
    }


    private static void listarTurmas() {
        if (turmas.isEmpty()) {
            System.out.println("Nenhuma turma cadastrada!");
            return;
        }
        
        for (Turma turma : turmas) {
            System.out.println("\nTurma: " + turma.getCodigo());
            System.out.println("Disciplina: " + turma.getDisciplina().getNome());
            System.out.println("Professor: " + turma.getProfessor());
            System.out.println("Vagas: " + turma.getAlunosMatriculados().size() + "/" + turma.getCapacidade());
            
            if (!turma.getAlunosMatriculados().isEmpty()) {
                System.out.println("\nAlunos matriculados:");
                turma.getAlunosMatriculados().forEach(a -> 
                    System.out.println(" - " + a.getNome() + " (" + a.getMatricula() + ")"));
            }
        }
    }

    public static List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public static List<Turma> getTurmas() {
        return turmas;
    }
}