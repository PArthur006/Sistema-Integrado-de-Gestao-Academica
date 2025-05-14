import aluno.Aluno;
import aluno.AlunoNormal;
import aluno.AlunoEspecial;
import disciplina.Disciplina;
import disciplina.Turma;

public class Main {
    public static void main(String[] args) {
        // Criando alunos
        Aluno aluno1 = new AlunoNormal("Pedro Silva", "123456789", "Engenharia");
        Aluno aluno2 = new AlunoEspecial("Ana Costa", "987654321", "Física");

        // Simular que aluno1 já concluiu POO
        aluno1.adicionarDisciplinaConcluida("INF101");

        // Criando disciplinas
        Disciplina poo = new Disciplina("POO", "INF101", 60);
        Disciplina ed = new Disciplina("Estrutura de Dados", "INF102", 60);
        ed.adicionarPreRequisito("INF101");

        // Criando turmas
        Turma turma1 = new Turma(poo, "Prof. João", "2024.1", "simples", true, "Sala 101", "Seg 10h", 3);
        Turma turma2 = new Turma(ed, "Prof. Maria", "2024.1", "ponderada", false, null, "Qua 14h", 2);

        System.out.println("--- Tentando matricular aluno1 em POO ---");
        if (turma1.adicionarAluno(aluno1)) {
            System.out.println("Matrícula de " + aluno1.getNome() + " em POO realizada com sucesso.");
        } else {
            System.out.println("Matrícula de " + aluno1.getNome() + " em POO falhou.");
        }

        System.out.println("\n--- Tentando matricular aluno1 em ED (com pré-requisito) ---");
        if (turma2.adicionarAluno(aluno1)) {
            System.out.println("Matrícula de " + aluno1.getNome() + " em ED realizada com sucesso.");
        } else {
            System.out.println("Matrícula de " + aluno1.getNome() + " em ED falhou.");
        }

        System.out.println("\n--- Tentando matricular aluno2 em POO ---");
        if (turma1.adicionarAluno(aluno2)) {
            System.out.println("Matrícula de " + aluno2.getNome() + " em POO realizada com sucesso.");
        } else {
            System.out.println("Matrícula de " + aluno2.getNome() + " em POO falhou.");
        }

        System.out.println("\n--- Tentando matricular aluno2 em ED (aluno especial pode cursar só 1) ---");
        if (turma2.adicionarAluno(aluno2)) {
            System.out.println("Matrícula de " + aluno2.getNome() + " em ED realizada com sucesso.");
        } else {
            System.out.println("Matrícula de " + aluno2.getNome() + " em ED falhou.");
        }

        // Mostrar alunos matriculados
        System.out.println("\n=========== Alunos matriculados nas turmas ===========\n");
        for (Turma turma : new Turma[]{turma1, turma2}) {
            System.out.println(turma);
            System.out.println("Alunos matriculados:");
            for (Aluno a : turma.getAlunosMatriculados()) {
                System.out.println("- " + a.getNome() + " | Matrícula: " + a.getMatricula());
            }
            System.out.println();
        }
    }
}
