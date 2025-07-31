package ui;

import aluno.Aluno;
import avaliacao.Frequencia;
import avaliacao.Nota;
import disciplina.Turma;

import java.util.Arrays;
import java.util.List;

public final class GeradorDeRelatorios {

    private GeradorDeRelatorios() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    public static String porTurma(Turma turma, List<Nota> notas, List<Frequencia> frequencias) {
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("Relatório da Turma: ").append(turma.getCodigo()).append("\n");
        relatorio.append("Disciplina: ").append(turma.getDisciplina().getNome()).append("\n");
        relatorio.append("Professor: ").append(turma.getProfessor()).append("\n\n");

        for (Aluno aluno : turma.getAlunosMatriculados()) {
            relatorio.append("Aluno: ").append(aluno.getNome()).append(" (").append(aluno.getMatricula()).append(")\n");

            notas.stream()
                .filter(n -> n.getTurma().equals(turma.getCodigo()) && n.getAluno().equals(aluno.getMatricula()))
                .findFirst()
                .ifPresent(n -> relatorio.append("  Notas: ").append(Arrays.toString(n.getNotas())).append("\n"));

            frequencias.stream()
                .filter(f -> f.getTurma().equals(turma.getCodigo()) && f.getAluno().equals(aluno.getMatricula()))
                .findFirst()
                .ifPresent(f -> relatorio.append("  Faltas: ").append(f.getFaltas()).append("\n"));
            relatorio.append("\n");
        }
        return relatorio.toString();
    }

    public static String porDisciplina(List<Turma> turmasDaDisciplina, String codigoDisciplina) {
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("Relatório da Disciplina: ").append(codigoDisciplina).append("\n\n");

        for (Turma turma : turmasDaDisciplina) {
            relatorio.append("Turma: ").append(turma.getCodigo()).append("\n");
            relatorio.append("  Professor: ").append(turma.getProfessor()).append("\n");
            relatorio.append("  Semestre: ").append(turma.getSemestre()).append("\n\n");
        }
        return relatorio.toString();
    }

    public static String porProfessor(List<Turma> turmasDoProfessor, String nomeProfessor) {
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("Relatório do Professor: ").append(nomeProfessor).append("\n\n");

        for (Turma turma : turmasDoProfessor) {
            relatorio.append("Disciplina: ").append(turma.getDisciplina().getNome()).append(" (").append(turma.getCodigo()).append(")\n");
            relatorio.append("  Semestre: ").append(turma.getSemestre()).append("\n\n");
        }
        return relatorio.toString();
    }
}