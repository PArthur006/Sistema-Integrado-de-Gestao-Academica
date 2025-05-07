import aluno.Aluno;
import aluno.AlunoNormal;
import disciplina.Disciplina;
import disciplina.Turma;

public class Main {
    public static void main(String[] args){
        // Criando algumas disciplinas
        Disciplina disciplina1 = new Disciplina("Matemática", "Mat101", 60);
        Disciplina disciplina2 = new Disciplina("Física", "FIS101", 80);

        //Criando turmas para as disciplinas
        Turma turma1 = new Turma(disciplina1, "João", "2025.1", "F1", true, "101", "08:00 - 10:00", 30);
        
        Turma turma2 = new Turma(disciplina1, "João", "2025.1", "F2", false, "", "18:00 - 20:00", 30);

        Turma turma3 = new Turma(disciplina2, "Maria", "2025.1", "F1", true, "202", "10:00 - 12:00", 25);

        // Adicionando turmas às disciplinas
        disciplina1.adicionarTurma(turma1);
        disciplina1.adicionarTurma(turma2);
        disciplina2.adicionarTurma(turma3);

        // Criando alunos
        Aluno aluno1 = new AlunoNormal("Pedro Silva", "123456789", "Engenharia");
        Aluno aluno2 = new AlunoNormal("Ana Costa", "987654321", "Física");

        // Matriculando alunos nas turmas
        turma1.adicionarAluno(aluno1);
        turma3.adicionarAluno(aluno2);

        //Exibindo informações
        System.out.println("---------------------------------------");
        System.out.println("Disciplinas e suas turmas:");
        System.out.println("---------------------------------------");
        System.out.println(disciplina1);
        for (Turma t : disciplina1.getTurmas()){
            System.out.println(t);
            System.out.println("Alunos matriculados: ");
            for(Aluno a : t.getAlunosMatriculados()){
                System.out.println(a.getNome());
            }
        }
        System.out.println("---------------------------------------");
        System.out.println(disciplina2);
        for(Turma t : disciplina2.getTurmas()){
            System.out.println(t);
            System.out.println("Alunos matriculados: ");
            for (Aluno a : t.getAlunosMatriculados()){
                System.out.println(a.getNome());
            }
        }
        System.out.println("---------------------------------------");
    }
}
