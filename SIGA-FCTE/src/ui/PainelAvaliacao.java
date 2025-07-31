package ui;

import aluno.Aluno;
import aluno.AlunoEspecial;
import avaliacao.Boletim;
import avaliacao.Frequencia;
import avaliacao.Nota;
import disciplina.Turma;
import persistencia.ArquivoAvaliacao;
import persistencia.TurmaRepository;

import javax.swing.*;
import java.util.ArrayList;
import java.awt.Frame;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PainelAvaliacao extends JPanel {
    private List<Turma> turmas;
    private Runnable acaoVoltar;

    public PainelAvaliacao(Runnable acaoVoltar) {
        this.acaoVoltar = acaoVoltar;
        this.turmas = TurmaRepository.getInstance().getTurmas();
        setLayout(new BorderLayout());
        setBackground(UIUtils.BRANCO);

        JPanel navbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navbar.setBackground(UIUtils.AZUL_ESCURO_1);
        JButton btnVoltar = UIUtils.criarBotao("⬅ Voltar", UIUtils.AZUL_ESCURO_2, Color.WHITE);
        JButton btnLancarNotas = UIUtils.criarBotao("📝 Lançar Notas", UIUtils.VERDE_1, Color.WHITE);
        JButton btnLancarFrequencia = UIUtils.criarBotao("✔️ Lançar Frequência", UIUtils.AZUL_ESCURO_2, Color.WHITE);
        JButton btnGerarBoletim = UIUtils.criarBotao("📋 Gerar Boletim", UIUtils.VERDE_2, Color.WHITE);
        JButton btnGerarRelatorio = UIUtils.criarBotao("📊 Gerar Relatório", UIUtils.AZUL_ESCURO_1, Color.WHITE);
        navbar.add(btnVoltar);
        navbar.add(btnLancarNotas);
        navbar.add(btnLancarFrequencia);
        navbar.add(btnGerarBoletim);
        navbar.add(btnGerarRelatorio);
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

    private Optional<Aluno> selecionarAlunoDaTurma(Turma turma) {
        String matricula = JOptionPane.showInputDialog(this, "Digite a matrícula do aluno:", "Selecionar Aluno", JOptionPane.QUESTION_MESSAGE);
        if (matricula == null || matricula.trim().isEmpty()) {
            return Optional.empty();
        }

        Optional<Aluno> alunoOpt = turma.getAlunosMatriculados().stream()
                .filter(a -> a.getMatricula().equalsIgnoreCase(matricula.trim()))
                .findFirst();

        if (alunoOpt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aluno não matriculado nesta turma!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return alunoOpt;
    }

    private void lancarNotas() {
        Optional<Turma> turmaOpt = encontrarTurma();
        if (turmaOpt.isEmpty()) return;
        Turma turma = turmaOpt.get();

        Optional<Aluno> alunoOpt = selecionarAlunoDaTurma(turma);
        if (alunoOpt.isEmpty()) return;

        Aluno aluno = alunoOpt.get();
        if (aluno instanceof AlunoEspecial) {
            JOptionPane.showMessageDialog(this, "Alunos Especiais não recebem notas, apenas frequência.", "Operação não Permitida", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        List<Nota> todasNotas = ArquivoAvaliacao.lerAvaliacoes();
        Optional<Nota> notaExistente = todasNotas.stream()
            .filter(n -> n.getTurma().equals(turma.getCodigo()) && n.getAluno().equals(aluno.getMatricula()))
            .findFirst();

        String[] labels = {"Prova 1:", "Prova 2:", "Prova 3:", "Listas de Exercício:", "Seminário:"};
        JComponent[] components = new JComponent[labels.length];
        for (int i = 0; i < labels.length; i++) {
            String valorInicial = "0.0";
            if (notaExistente.isPresent() && notaExistente.get().getNotas().length > i) {
                valorInicial = String.valueOf(notaExistente.get().getNotas()[i]);
            }
            components[i] = new JTextField(valorInicial);
        }

        FormularioDialog dialog = new FormularioDialog((Frame) SwingUtilities.getWindowAncestor(this), "Lançar Notas para " + aluno.getNome(), labels, components);
        dialog.setVisible(true);

        java.util.Map<String, String> valores = dialog.getValores();
        if (valores == null) return; // Usuário cancelou

        try {
            double p1 = Double.parseDouble(valores.get("Prova 1:"));
            double p2 = Double.parseDouble(valores.get("Prova 2:"));
            double p3 = Double.parseDouble(valores.get("Prova 3:"));
            double l = Double.parseDouble(valores.get("Listas de Exercício:"));
            double s = Double.parseDouble(valores.get("Seminário:"));
            Nota nota = new Nota(turma.getCodigo(), aluno.getMatricula(), new double[]{p1, p2, p3, l, s});

            // A lógica de persistência agora centraliza a atualização.
            // Apenas o novo registro precisa ser enviado.
            ArquivoAvaliacao.salvarAvaliacoes(List.of(nota));
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

        Optional<Aluno> alunoOpt = selecionarAlunoDaTurma(turma);
        if (alunoOpt.isEmpty()) return;

        String faltasStr = JOptionPane.showInputDialog(this,
                "Digite o número total de FALTAS do aluno.\n(Carga horária da disciplina: " + turma.getDisciplina().getCargaHoraria() + " aulas)", "0");

        // Validação explícita de cancelamento
        if (faltasStr == null) {
            return;
        }

        try {
            int cargaHoraria = turma.getDisciplina().getCargaHoraria();
            int faltas = Integer.parseInt(faltasStr);

            if (faltas < 0 || faltas > cargaHoraria) {
                JOptionPane.showMessageDialog(this, "Número de faltas inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Frequencia frequencia = new Frequencia(turma.getCodigo(), alunoOpt.get().getMatricula(), faltas);

            // A lógica de persistência agora centraliza a atualização.
            // Apenas o novo registro precisa ser enviado.
            ArquivoAvaliacao.salvarFrequencias(List.of(frequencia));
            JOptionPane.showMessageDialog(this, "Frequência lançada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor inválido. Por favor, insira um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void gerarBoletim() {
        String matricula = JOptionPane.showInputDialog(this, "Digite a matrícula do aluno:", "Gerar Boletim", JOptionPane.QUESTION_MESSAGE);
        if (matricula == null || matricula.trim().isEmpty()) return;

        // Encontra todas as turmas em que o aluno está matriculado
        List<Turma> turmasDoAluno = turmas.stream()
                .filter(t -> t.getAlunosMatriculados().stream()
                        .anyMatch(a -> a.getMatricula().equalsIgnoreCase(matricula.trim())))
                .collect(Collectors.toList());

        if (turmasDoAluno.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum registro encontrado para este aluno.", "Informação", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Pergunta o nível de detalhe do boletim
        String[] opcoes = {"Simples", "Detalhado"};
        int escolha = JOptionPane.showOptionDialog(
                this,
                "Selecione o nível de detalhe do boletim:",
                "Detalhes do Boletim",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[1] // Padrão para "Detalhado"
        );

        if (escolha == JOptionPane.CLOSED_OPTION) return; // Usuário fechou o diálogo
        boolean detalhado = (escolha == 1);

        // Agrupa as turmas por semestre
        java.util.Map<String, List<Turma>> turmasPorSemestre = turmasDoAluno.stream()
                .collect(Collectors.groupingBy(Turma::getSemestre));

        StringBuilder relatorio = new StringBuilder("Boletim do Aluno: " + matricula + "\n\n");

        List<Nota> notas = ArquivoAvaliacao.lerAvaliacoes();
        List<Frequencia> frequencias = ArquivoAvaliacao.lerFrequencias();

        // Ordena os semestres para exibição cronológica
        List<String> semestresOrdenados = new ArrayList<>(turmasPorSemestre.keySet());
        semestresOrdenados.sort(String.CASE_INSENSITIVE_ORDER);

        for (String semestre : semestresOrdenados) {
            relatorio.append("--- Semestre: ").append(semestre).append(" ---\n\n");
            for (Turma turma : turmasPorSemestre.get(semestre)) {
                Boletim boletim = new Boletim(turma, matricula, notas, frequencias);
                double cargaHoraria = turma.getDisciplina().getCargaHoraria();

                relatorio.append("Disciplina: ").append(turma.getDisciplina().getNome()).append("\n");
                if (detalhado) {
                    relatorio.append("  Professor: ").append(turma.getProfessor()).append("\n");
                    relatorio.append("  Modalidade: ").append(turma.getModalidade()).append("\n");
                    relatorio.append("  Turma: ").append(turma.getCodigo()).append("\n");
                }
                relatorio.append("Carga Horária: ").append(cargaHoraria).append(" aulas\n");
                relatorio.append("Média Final: ").append(String.format("%.2f", boletim.getMediaFinal())).append("\n");
                relatorio.append("Frequência: ").append(String.format("%.1f%%", boletim.getPercentualFrequencia())).append("\n");
                relatorio.append("Situação: ").append(boletim.getSituacao().getDescricao()).append("\n\n");
            }
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
        String conteudo = GeradorDeRelatorios.porTurma(turma, notas, frequencias);
        exibirRelatorio(conteudo, "Relatório da Turma " + turma.getCodigo());
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

        String conteudo = GeradorDeRelatorios.porDisciplina(turmasDaDisciplina, codigoDisciplina.trim());
        exibirRelatorio(conteudo, "Relatório da Disciplina " + codigoDisciplina.trim());
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

        String conteudo = GeradorDeRelatorios.porProfessor(turmasDoProfessor, nomeProfessor.trim());
        exibirRelatorio(conteudo, "Relatório do Professor " + nomeProfessor.trim());
    }
}