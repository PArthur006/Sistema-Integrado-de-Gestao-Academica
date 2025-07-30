package ui;

import disciplina.Disciplina;
import disciplina.Turma;
import persistencia.*;
import aluno.Aluno;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class PainelDisciplina extends JPanel {
    private static final Color AZUL_ESCURO_1 = Color.decode("#023373");
    private static final Color AZUL_ESCURO_2 = Color.decode("#023E73");
    private static final Color VERDE_1 = Color.decode("#02730A");
    private static final Color VERDE_2 = Color.decode("#055902");
    private static final Color BRANCO = Color.decode("#F2F2F2");

    private List<Disciplina> disciplinas;
    private DisciplinaTableModel tableModel;
    private JTable tabelaDisciplinas;
    private JTextField campoBusca;
    private Runnable acaoVoltar;

    public PainelDisciplina(Runnable acaoVoltar) {
        this.acaoVoltar = acaoVoltar;
        setLayout(new BorderLayout());
        setBackground(BRANCO);

        disciplinas = DisciplinaRepository.getInstance().getDisciplinas();

        JPanel navbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navbar.setBackground(AZUL_ESCURO_1);
        JButton btnCadastrar = criarBotao("Cadastrar Disciplina", VERDE_1, Color.WHITE);
        JButton btnEditar = criarBotao("Editar Selecionada", AZUL_ESCURO_2, Color.WHITE);
        JButton btnExcluir = criarBotao("Excluir Selecionada", VERDE_2, Color.WHITE);
        JButton btnTurmas = criarBotao("Gerenciar Turmas", AZUL_ESCURO_1, Color.WHITE);
        JButton btnVoltar = criarBotao("Voltar", AZUL_ESCURO_2, Color.WHITE);
        navbar.add(btnCadastrar);
        navbar.add(btnEditar);
        navbar.add(btnExcluir);
        navbar.add(btnTurmas);
        navbar.add(btnVoltar);
        add(navbar, BorderLayout.NORTH);

        campoBusca = new JTextField();
        campoBusca.setFont(new Font("SansSerif", Font.PLAIN, 16));
        campoBusca.setToolTipText("Buscar por nome ou código");

        JPanel painelCentro = new JPanel(new BorderLayout());
        painelCentro.setBackground(BRANCO);
        painelCentro.add(campoBusca, BorderLayout.NORTH);

        tableModel = new DisciplinaTableModel(disciplinas);
        tabelaDisciplinas = new JTable(tableModel);
        tabelaDisciplinas.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tabelaDisciplinas.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        tabelaDisciplinas.setRowHeight(28);
        JScrollPane scroll = new JScrollPane(tabelaDisciplinas);
        painelCentro.add(scroll, BorderLayout.CENTER);
        add(painelCentro, BorderLayout.CENTER);

        campoBusca.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            private void filtrar() {
                String texto = campoBusca.getText().toLowerCase();
                tableModel.setFiltro(texto);
            }
        });

        btnCadastrar.addActionListener(e -> cadastrarDisciplina());
        btnEditar.addActionListener(e -> editarDisciplina());
        btnExcluir.addActionListener(e -> excluirDisciplina());
        btnTurmas.addActionListener(e -> gerenciarTurmas());
        btnVoltar.addActionListener(e -> acaoVoltar.run());
    }

    private void cadastrarDisciplina() {
        JTextField campoNome = new JTextField();
        JTextField campoCodigo = new JTextField();
        JTextField campoCarga = new JTextField();
        JPanel painel = new JPanel(new GridLayout(0, 1));
        painel.add(new JLabel("Nome:"));
        painel.add(campoNome);
        painel.add(new JLabel("Código:"));
        painel.add(campoCodigo);
        painel.add(new JLabel("Carga Horária:"));
        painel.add(campoCarga);
        int result = JOptionPane.showConfirmDialog(this, painel, "Cadastrar Disciplina", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String nome = campoNome.getText().trim();
            String codigo = campoCodigo.getText().trim();
            int carga;
            try {
                carga = Integer.parseInt(campoCarga.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Carga horária inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (nome.isEmpty() || codigo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (disciplinas.stream().anyMatch(d -> d.getCodigo().equalsIgnoreCase(codigo))) {
                JOptionPane.showMessageDialog(this, "O código de disciplina '" + codigo + "' já existe. Por favor, escolha outro.", "Código Duplicado", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Disciplina nova = new Disciplina(nome, codigo, carga);
            disciplinas.add(nova);
            ArquivoDisciplina.salvarDisciplinas(disciplinas);
            tableModel.fireTableDataChanged();
            JOptionPane.showMessageDialog(this, "Disciplina cadastrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editarDisciplina() {
        int idx = tabelaDisciplinas.getSelectedRow();
        if (idx >= 0) {
            Disciplina d = tableModel.getDisciplina(idx);
            JTextField campoNome = new JTextField(d.getNome());
            JTextField campoCodigo = new JTextField(d.getCodigo());
            campoCodigo.setEditable(false);
            JTextField campoCarga = new JTextField(String.valueOf(d.getCargaHoraria()));
            JPanel painel = new JPanel(new GridLayout(0, 1));
            painel.add(new JLabel("Nome:"));
            painel.add(campoNome);
            painel.add(new JLabel("Código:"));
            painel.add(campoCodigo);
            painel.add(new JLabel("Carga Horária:"));
            painel.add(campoCarga);
            int result = JOptionPane.showConfirmDialog(this, painel, "Editar Disciplina", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String nome = campoNome.getText().trim();
                int carga;
                try {
                    carga = Integer.parseInt(campoCarga.getText().trim());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Carga horária inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (nome.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                for (Disciplina disc : disciplinas) {
                    if (disc.getCodigo().equals(d.getCodigo())) {
                        disc.setNome(nome);
                        disc.setCargaHoraria(carga);
                        break;
                    }
                }
                tableModel.setFiltro("");
                ArquivoDisciplina.salvarDisciplinas(disciplinas);
                tableModel.fireTableDataChanged();
                JOptionPane.showMessageDialog(this, "Disciplina editada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma disciplina para editar.", "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void excluirDisciplina() {
        int idx = tabelaDisciplinas.getSelectedRow();
        if (idx >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir a disciplina selecionada?\nTodas as turmas associadas também serão excluídas.", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Disciplina disciplinaParaExcluir = tableModel.getDisciplina(idx);
                String codigoDisciplina = disciplinaParaExcluir.getCodigo();

                List<Turma> todasAsTurmas = TurmaRepository.getInstance().getTurmas();
                todasAsTurmas.removeIf(t -> t.getDisciplina().getCodigo().equals(codigoDisciplina));
                ArquivoDisciplina.salvarTurmas(todasAsTurmas);

                disciplinas.removeIf(disc -> disc.getCodigo().equals(codigoDisciplina));
                ArquivoDisciplina.salvarDisciplinas(disciplinas);

                tableModel.setFiltro("");
                tableModel.fireTableDataChanged();
                JOptionPane.showMessageDialog(this, "Disciplina e suas turmas foram excluídas com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma disciplina para excluir.", "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void gerenciarTurmas() {
        int idx = tabelaDisciplinas.getSelectedRow();
        if (idx >= 0) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame == null) return;

            Disciplina d = tableModel.getDisciplina(idx);
            PainelTurmas painelTurmas = new PainelTurmas(d, () -> {
                frame.setContentPane(this);
                frame.revalidate();
                frame.repaint();
                tableModel.fireTableDataChanged();
            });
            frame.setContentPane(painelTurmas);
            frame.revalidate();
            frame.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma disciplina para gerenciar turmas.", "Atenção", JOptionPane.WARNING_MESSAGE);
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

    private static class DisciplinaTableModel extends AbstractTableModel {
        private final String[] colunas = {"Nome", "Código", "Carga Horária"};
        private List<Disciplina> disciplinas;
        private List<Disciplina> filtradas;
        public DisciplinaTableModel(List<Disciplina> disciplinas) {
            this.disciplinas = disciplinas;
            this.filtradas = disciplinas;
        }
        public void setFiltro(String filtro) {
            if (filtro.isEmpty()) {
                filtradas = disciplinas;
            } else {
                filtradas = disciplinas.stream().filter(d ->
                    d.getNome().toLowerCase().contains(filtro) ||
                    d.getCodigo().toLowerCase().contains(filtro)
                ).collect(Collectors.toList());
            }
            fireTableDataChanged();
        }
        public Disciplina getDisciplina(int idx) {
            return filtradas.get(idx);
        }
        @Override
        public int getRowCount() { return filtradas.size(); }
        @Override
        public int getColumnCount() { return colunas.length; }
        @Override
        public String getColumnName(int column) { return colunas[column]; }
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Disciplina d = filtradas.get(rowIndex);
            switch (columnIndex) {
                case 0: return d.getNome();
                case 1: return d.getCodigo();
                case 2: return d.getCargaHoraria();
                default: return "";
            }
        }
    }
}
