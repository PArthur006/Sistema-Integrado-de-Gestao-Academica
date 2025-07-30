package menus;

import disciplina.Disciplina;
import disciplina.Turma;
import persistencia.AlunoRepository;
import persistencia.ArquivoDisciplina;
import aluno.Aluno;


import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PainelTurmas extends JPanel {
    private static final Color AZUL_ESCURO_1 = Color.decode("#023373");
    private static final Color AZUL_ESCURO_2 = Color.decode("#023E73");
    private static final Color VERDE_1 = Color.decode("#02730A");
    private static final Color VERDE_2 = Color.decode("#055902");
    private static final Color BRANCO = Color.decode("#F2F2F2");

    private Disciplina disciplina;
    private List<Turma> turmas;
    private TurmaTableModel tableModel;
    private JTable tabelaTurmas;


    public PainelTurmas(Disciplina disciplina, Runnable acaoVoltar) {
        this.disciplina = disciplina;
        setLayout(new BorderLayout());
        setBackground(BRANCO);

        // Carregar turmas da disciplina
        turmas = ArquivoDisciplina.carregarTurmas().stream()
            .filter(t -> t.getDisciplina().getCodigo().equals(disciplina.getCodigo()))
            .collect(Collectors.toList());

        // Navbar
        JPanel navbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navbar.setBackground(AZUL_ESCURO_1);
        JButton btnCadastrar = criarBotao("Cadastrar Turma", VERDE_1, Color.WHITE);
        JButton btnEditar = criarBotao("Editar Selecionada", AZUL_ESCURO_2, Color.WHITE);
        JButton btnExcluir = criarBotao("Excluir Selecionada", VERDE_2, Color.WHITE);
        JButton btnMatricular = criarBotao("Matricular Aluno", VERDE_1, Color.WHITE);
        JButton btnVerAlunos = criarBotao("Ver Alunos", AZUL_ESCURO_1, Color.WHITE);
        JButton btnVoltar = criarBotao("Voltar", AZUL_ESCURO_2, Color.WHITE);
        navbar.add(btnCadastrar);
        navbar.add(btnEditar);
        navbar.add(btnExcluir);
        navbar.add(btnMatricular);
        navbar.add(btnVerAlunos);
        navbar.add(btnVoltar);
        add(navbar, BorderLayout.NORTH);

        // Tabela
        tableModel = new TurmaTableModel(turmas);
        tabelaTurmas = new JTable(tableModel);
        tabelaTurmas.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tabelaTurmas.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        tabelaTurmas.setRowHeight(28);
        JScrollPane scroll = new JScrollPane(tabelaTurmas);
        scroll.setPreferredSize(new Dimension(600, 250));
        add(scroll, BorderLayout.CENTER);

        // Ações
        btnCadastrar.addActionListener(e -> cadastrarTurma());
        btnEditar.addActionListener(e -> editarTurma());
        btnExcluir.addActionListener(e -> excluirTurma());
        btnMatricular.addActionListener(e -> matricularAluno());
        btnVerAlunos.addActionListener(e -> verAlunosMatriculados());
        btnVoltar.addActionListener(e -> acaoVoltar.run());
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
            return; // Usuário cancelou
        }

        // Encontra o aluno correspondente na lista de disponíveis
        int selectedIndex = java.util.Arrays.asList(opcoesAlunos).indexOf(alunoSelecionadoStr);
        Aluno alunoParaMatricular = alunosDisponiveis.get(selectedIndex);

        if (turmaSelecionada.matricularAluno(alunoParaMatricular)) {
            salvarTurmasGlobal();
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

        // Cria um JDialog customizado para exibir os alunos
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Alunos da Turma " + turma.getCodigo(), true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel painelLista = new JPanel();
        painelLista.setLayout(new BoxLayout(painelLista, BoxLayout.Y_AXIS));

        for (Aluno aluno : new ArrayList<>(alunosMatriculados)) {
            JPanel linhaAluno = new JPanel(new BorderLayout(10, 0));
            linhaAluno.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            linhaAluno.add(new JLabel(aluno.getNome() + " (" + aluno.getMatricula() + ")"), BorderLayout.CENTER);

            JButton btnDesmatricular = new JButton("Desmatricular");
            btnDesmatricular.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(dialog, "Desmatricular " + aluno.getNome() + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    turma.desmatricularAluno(aluno.getMatricula());
                    salvarTurmasGlobal();
                    tableModel.fireTableDataChanged(); // Atualiza a contagem de vagas na tabela principal

                    // Remove a linha da interface do dialog
                    painelLista.remove(linhaAluno);
                    painelLista.revalidate();
                    painelLista.repaint();
                    JOptionPane.showMessageDialog(dialog, "Aluno desmatriculado.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            linhaAluno.add(btnDesmatricular, BorderLayout.EAST);
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
        JTextField campoCodigo = new JTextField();
        JTextField campoProfessor = new JTextField();
        JTextField campoSemestre = new JTextField();
        JTextField campoSala = new JTextField();
        JTextField campoHorario = new JTextField();
        JTextField campoCapacidade = new JTextField();
        JTextField campoTipoAvaliacao = new JTextField();
        JPanel painel = new JPanel(new GridLayout(0, 1));
        painel.add(new JLabel("Código da Turma:"));
        painel.add(campoCodigo);
        painel.add(new JLabel("Professor:"));
        painel.add(campoProfessor);
        painel.add(new JLabel("Semestre:"));
        painel.add(campoSemestre);
        painel.add(new JLabel("Sala:"));
        painel.add(campoSala);
        painel.add(new JLabel("Horário:"));
        painel.add(campoHorario);
        painel.add(new JLabel("Capacidade:"));
        painel.add(campoCapacidade);
        painel.add(new JLabel("Tipo de Avaliação:"));
        painel.add(campoTipoAvaliacao);
        int result = JOptionPane.showConfirmDialog(this, painel, "Cadastrar Turma", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String codigo = campoCodigo.getText().trim();
            String professor = campoProfessor.getText().trim();
            String semestre = campoSemestre.getText().trim();
            String sala = campoSala.getText().trim();
            String horario = campoHorario.getText().trim();
            int capacidade;
            try {
                capacidade = Integer.parseInt(campoCapacidade.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Capacidade inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String tipoAvaliacao = campoTipoAvaliacao.getText().trim();
            if (codigo.isEmpty() || professor.isEmpty() || semestre.isEmpty() || sala.isEmpty() || horario.isEmpty() || tipoAvaliacao.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validação de código duplicado
            List<Turma> todasAsTurmas = ArquivoDisciplina.carregarTurmas();
            if (todasAsTurmas.stream().anyMatch(t -> t.getCodigo().equalsIgnoreCase(codigo))) {
                JOptionPane.showMessageDialog(this, "O código de turma '" + codigo + "' já existe. Por favor, escolha outro.", "Código Duplicado", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Turma nova = new Turma(codigo, disciplina, professor, semestre, sala, horario, capacidade, tipoAvaliacao);
            turmas.add(nova);
            salvarTurmasGlobal();
            tableModel.fireTableDataChanged();
            JOptionPane.showMessageDialog(this, "Turma cadastrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editarTurma() {
        int idx = tabelaTurmas.getSelectedRow();
        if (idx >= 0) {
            Turma t = tableModel.getTurma(idx);
            JTextField campoProfessor = new JTextField(t.getProfessor());
            JTextField campoSemestre = new JTextField(t.getSemestre());
            JTextField campoSala = new JTextField(t.getSala());
            JTextField campoHorario = new JTextField(t.getHorario());
            JTextField campoCapacidade = new JTextField(String.valueOf(t.getCapacidade()));
            JTextField campoTipoAvaliacao = new JTextField(t.getTipoAvaliacao());
            JPanel painel = new JPanel(new GridLayout(0, 1));
            painel.add(new JLabel("Professor:"));
            painel.add(campoProfessor);
            painel.add(new JLabel("Semestre:"));
            painel.add(campoSemestre);
            painel.add(new JLabel("Sala:"));
            painel.add(campoSala);
            painel.add(new JLabel("Horário:"));
            painel.add(campoHorario);
            painel.add(new JLabel("Capacidade:"));
            painel.add(campoCapacidade);
            painel.add(new JLabel("Tipo de Avaliação:"));
            painel.add(campoTipoAvaliacao);
            int result = JOptionPane.showConfirmDialog(this, painel, "Editar Turma", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String professor = campoProfessor.getText().trim();
                String semestre = campoSemestre.getText().trim();
                String sala = campoSala.getText().trim();
                String horario = campoHorario.getText().trim();
                int capacidade;
                try {
                    capacidade = Integer.parseInt(campoCapacidade.getText().trim());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Capacidade inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String tipoAvaliacao = campoTipoAvaliacao.getText().trim();
                if (professor.isEmpty() || semestre.isEmpty() || sala.isEmpty() || horario.isEmpty() || tipoAvaliacao.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                t.setProfessor(professor);
                t.setSemestre(semestre);
                t.setSala(sala);
                t.setHorario(horario);
                t.setCapacidade(capacidade);
                t.setTipoAvaliacao(tipoAvaliacao);
                salvarTurmasGlobal();
                tableModel.fireTableDataChanged();
                JOptionPane.showMessageDialog(this, "Turma editada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma turma para editar.", "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void excluirTurma() {
        int idx = tabelaTurmas.getSelectedRow();
        if (idx >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir a turma selecionada?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                turmas.remove(tableModel.getTurma(idx));
                salvarTurmasGlobal();
                tableModel.fireTableDataChanged();
                JOptionPane.showMessageDialog(this, "Turma excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma turma para excluir.", "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Salva todas as turmas do sistema, não só da disciplina atual
    private void salvarTurmasGlobal() {
        List<Turma> todas = ArquivoDisciplina.carregarTurmas();
        // Remove todas as turmas desta disciplina
        todas = todas.stream().filter(t -> !t.getDisciplina().getCodigo().equals(disciplina.getCodigo())).collect(Collectors.toList());
        // Adiciona as turmas atuais da disciplina
        todas.addAll(turmas);
        ArquivoDisciplina.salvarTurmas(todas);
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

    // Modelo de tabela para turmas
    private static class TurmaTableModel extends AbstractTableModel {
        private final String[] colunas = {"Código", "Professor", "Semestre", "Vagas", "Horário"};
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
                case 3: return t.getAlunosMatriculados().size() + "/" + t.getCapacidade();
                case 4: return t.getHorario();
                default: return "";
            }
        }
    }
}
