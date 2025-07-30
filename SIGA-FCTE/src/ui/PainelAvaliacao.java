package ui;

import aluno.Aluno;
import aluno.AlunoEspecial;
import avaliacao.Boletim;
import avaliacao.Frequencia;
import avaliacao.Nota;
import avaliacao.Relatorio;
import disciplina.Turma;
import persistencia.ArquivoAvaliacao;
import persistencia.TurmaRepository;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PainelAvaliacao extends JPanel {
    private static final Color AZUL_ESCURO_1 = Color.decode("#023373");
    private static final Color AZUL_ESCURO_2 = Color.decode("#023E73");
    private static final Color VERDE_1 = Color.decode("#02730A");
    private static final Color VERDE_2 = Color.decode("#055902");
    private static final Color BRANCO = Color.decode("#F2F2F2");

    private List<Turma> turmas;
    private Runnable acaoVoltar;

    public PainelAvaliacao(Runnable acaoVoltar) {
        this.acaoVoltar = acaoVoltar;
        this.turmas = TurmaRepository.getInstance().getTurmas();
        setLayout(new BorderLayout());
        setBackground(BRANCO);

        JPanel navbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navbar.setBackground(AZUL_ESCURO_1);
        JButton btnLancarNotas = criarBotao("Lançar Notas", VERDE_1, Color.WHITE);
        JButton btnLancarFrequencia = criarBotao("Lançar Frequência", AZUL_ESCURO_2, Color.WHITE);
        JButton btnGerarBoletim = criarBotao("Gerar Boletim", VERDE_2, Color.WHITE);
        JButton btnGerarRelatorio = criarBotao("Gerar Relatório", AZUL_ESCURO_1, Color.WHITE);
        JButton btnVoltar = criarBotao("Voltar", AZUL_ESCURO_2, Color.WHITE);
        navbar.add(btnLancarNotas);
        navbar.add(btnLancarFrequencia);
        navbar.add(btnGerarBoletim);
        navbar.add(btnGerarRelatorio);
        navbar.add(btnVoltar);
        add(navbar, BorderLayout.NORTH);

        btnLancarNotas.addActionListener(e -> lancarNotas());
        btnLancarFrequencia.addActionListener(e -> lancarFrequencias());
        btnGerarBoletim.addActionListener(e -> gerarBoletim());
        btnGerarRelatorio.addActionListener(e -> gerarRelatorio());
        btnVoltar.addActionListener(e -> acaoVoltar.run());

        JLabel info = new JLabel("Selecione uma opção no menu superior.", SwingConstants.CENTER);
        info.setFont(new Font("SansSerif", Font.ITALIC, 18));
        add(info, BorderLayout.CENTER);
    }

    private Optional<Turma> encontrarTurma() {
        String codigoTurma = JOptionPane.showInputDialog(this, "Digite o código da turma:", "Encontrar Turma", JOptionPane.QUESTION_MESSAGE);
        if (codigoTurma == null || codigoTurma.trim().isEmpty()) {
            return Optional.empty();
        }
        Optional<Turma> turmaOpt = turmas.stream()
                .filter(t -> t.getCodigo().equalsIgnoreCase(codigoTurma.trim()))
                .findFirst();
        if (turmaOpt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Turma não encontrada!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return turmaOpt;
    }

    private void lancarNotas() {
        Optional<Turma> turmaOpt = encontrarTurma();
        if (turmaOpt.isEmpty()) return;
        Turma turma = turmaOpt.get();

        String matricula = JOptionPane.showInputDialog(this, "Digite a matrícula do aluno:", "Lançar Notas", JOptionPane.QUESTION_MESSAGE);
        if (matricula == null || matricula.trim().isEmpty()) return;

        Optional<Aluno> alunoOpt = turma.getAlunosMatriculados().stream().filter(a -> a.getMatricula().equals(matricula)).findFirst();
        if (alunoOpt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aluno não matriculado nesta turma!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Aluno aluno = alunoOpt.get();
        if (aluno instanceof AlunoEspecial && aluno.getDisciplinasMatriculadas().size() >= 2) {
            JOptionPane.showMessageDialog(this, "Aluno Especial não pode receber notas em mais de 2 disciplinas!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double p1 = Double.parseDouble(JOptionPane.showInputDialog(this, "Nota P1:", "0"));
            double p2 = Double.parseDouble(JOptionPane.showInputDialog(this, "Nota P2:", "0"));
            double p3 = Double.parseDouble(JOptionPane.showInputDialog(this, "Nota P3:", "0"));
            double l = Double.parseDouble(JOptionPane.showInputDialog(this, "Nota L:", "0"));
            double s = Double.parseDouble(JOptionPane.showInputDialog(this, "Nota S:", "0"));

            Nota nota = new Nota(turma.getCodigo(), matricula, new double[]{p1, p2, p3, l, s});

            List<Nota> todasNotas = ArquivoAvaliacao.lerAvaliacoes();
            todasNotas.removeIf(n -> n.getTurma().equals(turma.getCodigo()) && n.getAluno().equals(matricula));
            todasNotas.add(nota);
            ArquivoAvaliacao.salvarAvaliacoes(todasNotas);
            JOptionPane.showMessageDialog(this, "Notas lançadas com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Nota inválida. Por favor, insira um número.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException e) {
        }
    }

    private void lancarFrequencias() {
        Optional<Turma> turmaOpt = encontrarTurma();
        if (turmaOpt.isEmpty()) return;
        Turma turma = turmaOpt.get();

        String matricula = JOptionPane.showInputDialog(this, "Digite a matrícula do aluno:", "Lançar Frequência", JOptionPane.QUESTION_MESSAGE);
        if (matricula == null || matricula.trim().isEmpty()) return;

        if (turma.getAlunosMatriculados().stream().noneMatch(a -> a.getMatricula().equals(matricula))) {
            JOptionPane.showMessageDialog(this, "Aluno não matriculado nesta turma!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int totalAulas = Integer.parseInt(JOptionPane.showInputDialog(this, "Total de aulas ministradas:", "0"));
            int presencas = Integer.parseInt(JOptionPane.showInputDialog(this, "Número de presenças do aluno:", "0"));

            int faltas = totalAulas - presencas;
            Frequencia frequencia = new Frequencia(turma.getCodigo(), matricula, faltas);

            List<Frequencia> todasFrequencias = ArquivoAvaliacao.lerFrequencias();
            todasFrequencias.removeIf(f -> f.getTurma().equals(turma.getCodigo()) && f.getAluno().equals(matricula));
            todasFrequencias.add(frequencia);
            ArquivoAvaliacao.salvarFrequencias(todasFrequencias);
            JOptionPane.showMessageDialog(this, "Frequência lançada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor inválido. Por favor, insira um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException e) {
        }
    }

    private void gerarBoletim() {
        String matricula = JOptionPane.showInputDialog(this, "Digite a matrícula do aluno:", "Gerar Boletim", JOptionPane.QUESTION_MESSAGE);
        if (matricula == null || matricula.trim().isEmpty()) return;

        StringBuilder relatorio = new StringBuilder("Boletim do Aluno: " + matricula + "\n\n");
        boolean encontrou = false;

        List<Nota> notas = ArquivoAvaliacao.lerAvaliacoes();
        List<Frequencia> frequencias = ArquivoAvaliacao.lerFrequencias();

        for (Turma turma : turmas) {
            if (turma.getAlunosMatriculados().stream().anyMatch(a -> a.getMatricula().equals(matricula))) {
                Boletim boletim = new Boletim(turma, matricula, notas, frequencias);
                relatorio.append("Disciplina: ").append(turma.getDisciplina().getNome()).append("\n");
                relatorio.append("Média Final: ").append(String.format("%.2f", boletim.getMediaFinal())).append("\n");
                relatorio.append("Frequência: ").append(String.format("%.1f%%", boletim.getPercentualFrequencia())).append("\n");
                relatorio.append("Situação: ").append(boletim.isAprovado() ? "Aprovado" : "Reprovado").append("\n\n");
                encontrou = true;
            }
        }

        if (!encontrou) {
            relatorio.append("Nenhum registro encontrado para este aluno.");
        }

        exibirRelatorio(relatorio.toString(), "Boletim do Aluno: " + matricula);
    }

    private void gerarRelatorio() {
        String[] opcoes = {"Por Turma", "Por Disciplina", "Por Professor"};
        String escolha = (String) JOptionPane.showInputDialog(
                this,
                "Selecione o tipo de relatório:",
                "Gerar Relatório",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (escolha == null) {
            return;
        }

        if ("Por Turma".equals(escolha)) {
            gerarRelatorioPorTurma();
        } else if ("Por Disciplina".equals(escolha)) {
            gerarRelatorioPorDisciplina();
        } else {
            gerarRelatorioPorProfessor();
        }
    }

    private JButton criarBotao(String texto, Color corFundo, Color corTexto) {
        JButton botao = new JButton(texto);
        botao.setBackground(corFundo);
        botao.setForeground(corTexto);
        botao.setFocusPainted(false);
        botao.setFont(new Font("SansSerif", Font.BOLD, 16));
        botao.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return botao;
    }

    private void gerarRelatorioPorTurma() {
        String codigoTurma = JOptionPane.showInputDialog(this, "Digite o código da turma:", "Relatório por Turma", JOptionPane.QUESTION_MESSAGE);
        if (codigoTurma == null || codigoTurma.trim().isEmpty()) {
            return;
        }

        Optional<Turma> turmaOpt = turmas.stream()
                .filter(t -> t.getCodigo().equalsIgnoreCase(codigoTurma.trim()))
                .findFirst();

        if (turmaOpt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Turma não encontrada!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Turma turma = turmaOpt.get();
        List<Nota> notas = ArquivoAvaliacao.lerAvaliacoes();
        List<Frequencia> frequencias = ArquivoAvaliacao.lerFrequencias();

        StringBuilder relatorio = new StringBuilder();
        relatorio.append("Relatório da Turma: ").append(turma.getCodigo()).append("\n");
        relatorio.append("Disciplina: ").append(turma.getDisciplina().getNome()).append("\n");
        relatorio.append("Professor: ").append(turma.getProfessor()).append("\n\n");

        for (Aluno aluno : turma.getAlunosMatriculados()) {
            relatorio.append("Aluno: ").append(aluno.getNome()).append(" (").append(aluno.getMatricula()).append(")\n");

            notas.stream()
                .filter(n -> n.getTurma().equals(turma.getCodigo()) && n.getAluno().equals(aluno.getMatricula()))
                .findFirst()
                .ifPresent(n -> relatorio.append("  Notas: ").append(java.util.Arrays.toString(n.getNotas())).append("\n"));

            frequencias.stream()
                .filter(f -> f.getTurma().equals(turma.getCodigo()) && f.getAluno().equals(aluno.getMatricula()))
                .findFirst()
                .ifPresent(f -> relatorio.append("  Faltas: ").append(f.getFaltas()).append("\n"));
            relatorio.append("\n");
        }

        exibirRelatorio(relatorio.toString(), "Relatório da Turma " + turma.getCodigo());
    }

    private void exibirRelatorio(String conteudo, String titulo) {
        JTextArea textArea = new JTextArea(conteudo);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        JOptionPane.showMessageDialog(this, scrollPane, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private void gerarRelatorioPorDisciplina() {
        String codigoDisciplina = JOptionPane.showInputDialog(this, "Digite o código da disciplina:", "Relatório por Disciplina", JOptionPane.QUESTION_MESSAGE);
        if (codigoDisciplina == null || codigoDisciplina.trim().isEmpty()) {
            return;
        }

        List<Turma> turmasDaDisciplina = turmas.stream()
                .filter(t -> t.getDisciplina().getCodigo().equalsIgnoreCase(codigoDisciplina.trim()))
                .collect(Collectors.toList());

        if (turmasDaDisciplina.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma turma encontrada para esta disciplina.", "Informação", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder relatorio = new StringBuilder();
        relatorio.append("Relatório da Disciplina: ").append(codigoDisciplina.trim()).append("\n\n");

        for (Turma turma : turmasDaDisciplina) {
            relatorio.append("Turma: ").append(turma.getCodigo()).append("\n");
            relatorio.append("  Professor: ").append(turma.getProfessor()).append("\n");
            relatorio.append("  Semestre: ").append(turma.getSemestre()).append("\n\n");
        }

        exibirRelatorio(relatorio.toString(), "Relatório da Disciplina " + codigoDisciplina.trim());
    }

    private void gerarRelatorioPorProfessor() {
        String nomeProfessor = JOptionPane.showInputDialog(this, "Digite o nome do professor:", "Relatório por Professor", JOptionPane.QUESTION_MESSAGE);
        if (nomeProfessor == null || nomeProfessor.trim().isEmpty()) {
            return;
        }

        List<Turma> turmasDoProfessor = turmas.stream()
                .filter(t -> t.getProfessor().equalsIgnoreCase(nomeProfessor.trim()))
                .collect(Collectors.toList());

        if (turmasDoProfessor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma turma encontrada para este professor.", "Informação", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder relatorio = new StringBuilder();
        relatorio.append("Relatório do Professor: ").append(nomeProfessor.trim()).append("\n\n");

        for (Turma turma : turmasDoProfessor) {
            relatorio.append("Disciplina: ").append(turma.getDisciplina().getNome()).append(" (").append(turma.getCodigo()).append(")\n");
            relatorio.append("  Semestre: ").append(turma.getSemestre()).append("\n\n");
        }

        exibirRelatorio(relatorio.toString(), "Relatório do Professor " + nomeProfessor.trim());
    }
}