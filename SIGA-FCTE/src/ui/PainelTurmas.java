package ui;

import disciplina.Disciplina;
import disciplina.Turma;
import persistencia.ArquivoDisciplina;
import persistencia.AlunoRepository;
import persistencia.TurmaRepository;
import aluno.Aluno;


import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.Frame;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

public class PainelTurmas extends JPanel {
    private Disciplina disciplina;
    private List<Turma> turmas;
    private TurmaTableModel tableModel;
    private JTable tabelaTurmas;


    public PainelTurmas(Disciplina disciplina, Runnable acaoVoltar) {
        this.disciplina = disciplina;
        setLayout(new BorderLayout());
        setBackground(UIUtils.BRANCO);

        turmas = TurmaRepository.getInstance().getTurmas().stream()
            .filter(t -> t.getDisciplina().getCodigo().equals(disciplina.getCodigo()))
            .collect(Collectors.toList());

        JPanel navbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navbar.setBackground(UIUtils.AZUL_ESCURO_1);
        JButton btnCadastrar = UIUtils.criarBotao("Cadastrar Turma", UIUtils.VERDE_1, Color.WHITE);
        JButton btnEditar = UIUtils.criarBotao("Editar Selecionada", UIUtils.AZUL_ESCURO_2, Color.WHITE);
        JButton btnExcluir = UIUtils.criarBotao("Excluir Selecionada", UIUtils.VERDE_2, Color.WHITE);
        JButton btnMatricular = UIUtils.criarBotao("Matricular Aluno", UIUtils.VERDE_1, Color.WHITE);
        JButton btnTrancar = UIUtils.criarBotao("Trancar Disciplina", UIUtils.VERDE_2, Color.WHITE);
        JButton btnVerAlunos = UIUtils.criarBotao("Ver Alunos", UIUtils.AZUL_ESCURO_1, Color.WHITE);
        JButton btnVoltar = UIUtils.criarBotao("Voltar", UIUtils.AZUL_ESCURO_2, Color.WHITE);
        navbar.add(btnCadastrar);
        navbar.add(btnEditar);
        navbar.add(btnExcluir);
        navbar.add(btnMatricular);
        navbar.add(btnTrancar);
        navbar.add(btnVerAlunos);
        navbar.add(btnVoltar);
        add(navbar, BorderLayout.NORTH);

        tableModel = new TurmaTableModel(turmas);
        tabelaTurmas = new JTable(tableModel);
        tabelaTurmas.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tabelaTurmas.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        tabelaTurmas.setRowHeight(28);
        JScrollPane scroll = new JScrollPane(tabelaTurmas);
        add(scroll, BorderLayout.CENTER);

        btnCadastrar.addActionListener(e -> cadastrarTurma());
        btnEditar.addActionListener(e -> editarTurma());
        btnExcluir.addActionListener(e -> excluirTurma());
        btnMatricular.addActionListener(e -> matricularAluno());
        btnTrancar.addActionListener(e -> trancarDisciplina());
        btnVerAlunos.addActionListener(e -> verAlunosMatriculados());
        btnVoltar.addActionListener(e -> acaoVoltar.run());
    }

    private void trancarDisciplina() {
        int idx = tabelaTurmas.getSelectedRow();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma turma.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Turma turmaSelecionada = tableModel.getTurma(idx);
        List<Aluno> alunosMatriculados = turmaSelecionada.getAlunosMatriculados();

        if (alunosMatriculados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há alunos matriculados nesta turma para trancar.", "Informação", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] opcoesAlunos = alunosMatriculados.stream()
                .map(a -> a.getNome() + " (" + a.getMatricula() + ")")
                .toArray(String[]::new);

        String alunoSelecionadoStr = (String) JOptionPane.showInputDialog(this, "Selecione o aluno para trancar a disciplina:", "Trancar Disciplina", JOptionPane.PLAIN_MESSAGE, null, opcoesAlunos, opcoesAlunos[0]);

        if (alunoSelecionadoStr == null) return;

        int selectedIndex = java.util.Arrays.asList(opcoesAlunos).indexOf(alunoSelecionadoStr);
        Aluno alunoParaTrancar = alunosMatriculados.get(selectedIndex);

        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja trancar a disciplina para " + alunoParaTrancar.getNome() + "?", "Confirmar Trancamento", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            turmaSelecionada.desmatricularAluno(alunoParaTrancar.getMatricula());
            ArquivoDisciplina.salvarTurmas(TurmaRepository.getInstance().getTurmas());
            tableModel.fireTableDataChanged();
            JOptionPane.showMessageDialog(this, "Disciplina trancada com sucesso para o aluno.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void matricularAluno() {
        int idx = tabelaTurmas.getSelectedRow();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma turma para matricular alunos.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Turma turmaSelecionada = tableModel.getTurma(idx);

        if (turmaSelecionada.getAlunosMatriculados().size() >= turmaSelecionada.getCapacidade()) {
            JOptionPane.showMessageDialog(this, "A turma está lotada.", "Turma Lotada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Aluno> todosAlunos = AlunoRepository.getInstance().getAlunos();
        List<Aluno> alunosDisponiveis = todosAlunos.stream()
                .filter(aluno -> turmaSelecionada.getAlunosMatriculados().stream()
                        .noneMatch(matriculado -> matriculado.getMatricula().equals(aluno.getMatricula())))
                .collect(Collectors.toList());

        if (alunosDisponiveis.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há alunos disponíveis para matrícula.", "Informação", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] opcoesAlunos = alunosDisponiveis.stream()
                .map(a -> a.getNome() + " (" + a.getMatricula() + ")")
                .toArray(String[]::new);

        String alunoSelecionadoStr = (String) JOptionPane.showInputDialog(
                this,
                "Selecione o aluno para matricular:",
                "Matricular Aluno na Turma " + turmaSelecionada.getCodigo(),
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoesAlunos,
                opcoesAlunos[0]
        );

        if (alunoSelecionadoStr == null) {
            return;
        }

        int selectedIndex = java.util.Arrays.asList(opcoesAlunos).indexOf(alunoSelecionadoStr);
        Aluno alunoParaMatricular = alunosDisponiveis.get(selectedIndex);

        // --- INÍCIO DA VALIDAÇÃO PARA ALUNO ESPECIAL ---
        if (alunoParaMatricular instanceof aluno.AlunoEspecial) {
            long matriculasAtuais = TurmaRepository.getInstance().getTurmas().stream()
                .filter(turma -> turma.getAlunosMatriculados().stream()
                    .anyMatch(aluno -> aluno.getMatricula().equals(alunoParaMatricular.getMatricula())))
                .count();

            if (matriculasAtuais >= 2) {
                String msg = "Alunos Especiais não podem se matricular em mais de 2 disciplinas.\n" +
                             "O aluno já está matriculado em " + matriculasAtuais + " disciplina(s).";
                JOptionPane.showMessageDialog(this, msg, "Limite de Matrícula Excedido", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        // --- FIM DA VALIDAÇÃO ---

        // --- INÍCIO DA VALIDAÇÃO DE PRÉ-REQUISITOS ---
        List<String> preRequisitos = turmaSelecionada.getDisciplina().getPreRequisitos();
        List<String> disciplinasConcluidas = alunoParaMatricular.getDisciplinasConcluidas();

        List<String> faltantes = preRequisitos.stream()
                .filter(preReq -> !disciplinasConcluidas.contains(preReq))
                .collect(Collectors.toList());

        if (!faltantes.isEmpty()) {
            String msg = "O aluno não cumpre os seguintes pré-requisitos:\n" + String.join(", ", faltantes);
            JOptionPane.showMessageDialog(this, msg, "Pré-requisitos não Atendidos", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // --- FIM DA VALIDAÇÃO ---

        if (turmaSelecionada.matricularAluno(alunoParaMatricular)) {
            ArquivoDisciplina.salvarTurmas(TurmaRepository.getInstance().getTurmas());
            tableModel.fireTableDataChanged();
            JOptionPane.showMessageDialog(this, "Aluno matriculado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Não foi possível matricular o aluno.\nVerifique se o aluno cumpre os pré-requisitos ou se a turma está lotada.", "Falha na Matrícula", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verAlunosMatriculados() {
        int idx = tabelaTurmas.getSelectedRow();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma turma para ver os alunos.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Turma turma = tableModel.getTurma(idx);
        List<Aluno> alunosMatriculados = turma.getAlunosMatriculados();

        if (alunosMatriculados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há alunos matriculados nesta turma.", "Informação", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Alunos da Turma " + turma.getCodigo(), true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel painelLista = new JPanel();
        painelLista.setLayout(new BoxLayout(painelLista, BoxLayout.Y_AXIS));

        for (Aluno aluno : new ArrayList<>(alunosMatriculados)) {
            JPanel linhaAluno = new JPanel(new BorderLayout(10, 0));
            linhaAluno.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            linhaAluno.add(new JLabel(aluno.getNome() + " (" + aluno.getMatricula() + ")"), BorderLayout.CENTER);

            JButton btnTrancar = new JButton("Trancar");
            btnTrancar.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(dialog, "Trancar a disciplina para " + aluno.getNome() + "?", "Confirmar Trancamento", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    turma.desmatricularAluno(aluno.getMatricula());
                    ArquivoDisciplina.salvarTurmas(TurmaRepository.getInstance().getTurmas());
                    tableModel.fireTableDataChanged(); // Atualiza a contagem de vagas na tabela principal

                    // Remove a linha da interface do diálogo
                    painelLista.remove(linhaAluno);
                    painelLista.revalidate();
                    painelLista.repaint();
                    JOptionPane.showMessageDialog(dialog, "Disciplina trancada para o aluno.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            linhaAluno.add(btnTrancar, BorderLayout.EAST);
            painelLista.add(linhaAluno);
        }

        JScrollPane scrollPane = new JScrollPane(painelLista);
        dialog.add(scrollPane, BorderLayout.CENTER);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dialog.dispose());
        dialog.add(btnFechar, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void cadastrarTurma() {
        // Componentes do formulário
        JTextField campoCodigo = new JTextField();
        JTextField campoProfessor = new JTextField();
        JTextField campoSemestre = new JTextField();
        JTextField campoSala = new JTextField();
        JTextField campoHorario = new JTextField();
        JTextField campoCapacidade = new JTextField();
        JComboBox<String> comboTipoAvaliacao = new JComboBox<>(new String[]{"Simples", "Ponderada"});
        JComboBox<String> comboModalidade = new JComboBox<>(new String[]{"Presencial", "Remota"});

        // Lógica para habilitar/desabilitar o campo de sala
        comboModalidade.addActionListener(e -> {
            boolean isPresencial = "Presencial".equals(comboModalidade.getSelectedItem());
            campoSala.setEnabled(isPresencial);
            if (!isPresencial) campoSala.setText("N/A");
        });

        String[] labels = {"Código da Turma:", "Professor:", "Semestre:", "Modalidade:", "Sala:", "Horário:", "Capacidade:", "Tipo de Avaliação:"};
        JComponent[] components = {campoCodigo, campoProfessor, campoSemestre, comboModalidade, campoSala, campoHorario, campoCapacidade, comboTipoAvaliacao};
        FormularioDialog dialog = new FormularioDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cadastrar Turma", labels, components);
        dialog.setVisible(true);

        Map<String, String> valores = dialog.getValores();

        if (valores != null) {
            String codigo = valores.get("Código da Turma:");
            String professor = valores.get("Professor:");
            String semestre = valores.get("Semestre:");
            String modalidade = valores.get("Modalidade:");
            String sala = valores.get("Sala:");
            String horario = valores.get("Horário:");
            String capacidadeStr = valores.get("Capacidade:");
            String tipoAvaliacao = valores.get("Tipo de Avaliação:");

            if (codigo.isEmpty() || professor.isEmpty() || semestre.isEmpty() || sala.isEmpty() || horario.isEmpty() || capacidadeStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int capacidade;
            try {
                capacidade = Integer.parseInt(capacidadeStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Capacidade inválida! Insira um número.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validação de código duplicado
            if (TurmaRepository.getInstance().getTurmas().stream().anyMatch(t -> t.getCodigo().equalsIgnoreCase(codigo))) {
                JOptionPane.showMessageDialog(this, "O código de turma '" + codigo + "' já existe. Por favor, escolha outro.", "Código Duplicado", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validação de conflito de horário para professor e sala
            if (!horario.equalsIgnoreCase("N/A")) { // Não checar se não há horário definido
                for (Turma existente : TurmaRepository.getInstance().getTurmas()) {
                    if (existente.getSemestre().equalsIgnoreCase(semestre) && existente.getHorario().equalsIgnoreCase(horario)) {
                        // Conflito de professor
                        if (existente.getProfessor().equalsIgnoreCase(professor)) {
                            JOptionPane.showMessageDialog(this, "Conflito de horário: O professor '" + professor + "' já está alocado neste horário para a turma " + existente.getCodigo() + ".", "Conflito de Alocação", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        // Conflito de sala (apenas se ambas forem presenciais)
                        if (modalidade.equals("Presencial") && existente.getModalidade().equals("Presencial") && existente.getSala().equalsIgnoreCase(sala)) {
                            JOptionPane.showMessageDialog(this, "Conflito de horário: A sala '" + sala + "' já está em uso neste horário pela turma " + existente.getCodigo() + ".", "Conflito de Alocação", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }
            }

            Turma nova = new Turma(codigo, disciplina, professor, semestre, sala, horario, capacidade, tipoAvaliacao, modalidade);
            TurmaRepository.getInstance().getTurmas().add(nova); // Adiciona na lista global
            turmas.add(nova); // Adiciona na lista local para exibição
            ArquivoDisciplina.salvarTurmas(TurmaRepository.getInstance().getTurmas());
            tableModel.fireTableDataChanged();
            JOptionPane.showMessageDialog(this, "Turma cadastrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editarTurma() {
        int idx = tabelaTurmas.getSelectedRow();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma turma para editar.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

            Turma t = tableModel.getTurma(idx);

            // Componentes do formulário com valores iniciais
            JTextField campoCodigo = new JTextField(t.getCodigo());
            campoCodigo.setEditable(false);
            JTextField campoProfessor = new JTextField(t.getProfessor());
            JTextField campoSemestre = new JTextField(t.getSemestre());
            JTextField campoSala = new JTextField(t.getSala());
            JTextField campoHorario = new JTextField(t.getHorario());
            JTextField campoCapacidade = new JTextField(String.valueOf(t.getCapacidade()));
            JComboBox<String> comboTipoAvaliacao = new JComboBox<>(new String[]{"Simples", "Ponderada"});
            comboTipoAvaliacao.setSelectedItem(t.getTipoAvaliacao());
            JComboBox<String> comboModalidade = new JComboBox<>(new String[]{"Presencial", "Remota"});
            comboModalidade.setSelectedItem(t.getModalidade());

            comboModalidade.addActionListener(e -> {
                boolean isPresencial = "Presencial".equals(comboModalidade.getSelectedItem());
                campoSala.setEnabled(isPresencial);
                if (!isPresencial) campoSala.setText("N/A");
            });
            comboModalidade.getActionListeners()[0].actionPerformed(null); // Executa uma vez para definir o estado inicial

            String[] labels = {"Código da Turma:", "Professor:", "Semestre:", "Modalidade:", "Sala:", "Horário:", "Capacidade:", "Tipo de Avaliação:"};
            JComponent[] components = {campoCodigo, campoProfessor, campoSemestre, comboModalidade, campoSala, campoHorario, campoCapacidade, comboTipoAvaliacao};
            FormularioDialog dialog = new FormularioDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Turma", labels, components);
            dialog.setVisible(true);

            Map<String, String> valores = dialog.getValores();

            if (valores != null) {
                String professor = valores.get("Professor:");
                String semestre = valores.get("Semestre:");
                String modalidade = valores.get("Modalidade:");
                String sala = "Presencial".equals(modalidade) ? valores.get("Sala:") : "N/A";
                String horario = valores.get("Horário:");
                String capacidadeStr = valores.get("Capacidade:");
                String tipoAvaliacao = valores.get("Tipo de Avaliação:");

                if (professor.isEmpty() || semestre.isEmpty() || sala.isEmpty() || horario.isEmpty() || capacidadeStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int capacidade;
                try {
                    capacidade = Integer.parseInt(capacidadeStr);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Capacidade inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validação de conflito de horário para professor e sala
                if (!horario.equalsIgnoreCase("N/A")) {
                    for (Turma existente : TurmaRepository.getInstance().getTurmas()) {
                        // Ignora a própria turma que está sendo editada
                        if (existente.getCodigo().equalsIgnoreCase(t.getCodigo())) {
                            continue;
                        }

                        if (existente.getSemestre().equalsIgnoreCase(semestre) && existente.getHorario().equalsIgnoreCase(horario)) {
                            if (existente.getProfessor().equalsIgnoreCase(professor)) {
                                JOptionPane.showMessageDialog(this, "Conflito de horário: O professor '" + professor + "' já está alocado neste horário para a turma " + existente.getCodigo() + ".", "Conflito de Alocação", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            if (modalidade.equals("Presencial") && existente.getModalidade().equals("Presencial") && existente.getSala().equalsIgnoreCase(sala)) {
                                JOptionPane.showMessageDialog(this, "Conflito de horário: A sala '" + sala + "' já está em uso neste horário pela turma " + existente.getCodigo() + ".", "Conflito de Alocação", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                    }
                }

                t.setProfessor(professor);
                t.setSemestre(semestre);
                t.setSala(sala);
                t.setHorario(horario);
                t.setCapacidade(capacidade);
                t.setTipoAvaliacao(tipoAvaliacao);
                t.setModalidade(modalidade);
                ArquivoDisciplina.salvarTurmas(TurmaRepository.getInstance().getTurmas());
                tableModel.fireTableDataChanged();
                JOptionPane.showMessageDialog(this, "Turma editada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
    }

    private void excluirTurma() {
        int idx = tabelaTurmas.getSelectedRow();
        if (idx >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir a turma selecionada?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Turma turmaParaRemover = tableModel.getTurma(idx);
                turmas.remove(turmaParaRemover); // Remove da lista local
                TurmaRepository.getInstance().getTurmas().remove(turmaParaRemover); // Remove da lista global
                ArquivoDisciplina.salvarTurmas(TurmaRepository.getInstance().getTurmas());
                tableModel.fireTableDataChanged();
                JOptionPane.showMessageDialog(this, "Turma excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma turma para excluir.", "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Modelo de tabela para turmas
    private static class TurmaTableModel extends AbstractTableModel {
        private final String[] colunas = {"Código", "Professor", "Semestre", "Modalidade", "Vagas", "Horário"};
        private List<Turma> turmas;
        public TurmaTableModel(List<Turma> turmas) {
            this.turmas = turmas;
        }
        public Turma getTurma(int idx) {
            return turmas.get(idx);
        }
        @Override
        public int getRowCount() { return turmas.size(); }
        @Override
        public int getColumnCount() { return colunas.length; }
        @Override
        public String getColumnName(int column) { return colunas[column]; }
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Turma t = turmas.get(rowIndex);
            switch (columnIndex) {
                case 0: return t.getCodigo();
                case 1: return t.getProfessor();
                case 2: return t.getSemestre();
                case 3: return t.getModalidade();
                case 4: return t.getAlunosMatriculados().size() + "/" + t.getCapacidade();
                case 5: return t.getHorario();
                default: return "";
            }
        }
    }
}
