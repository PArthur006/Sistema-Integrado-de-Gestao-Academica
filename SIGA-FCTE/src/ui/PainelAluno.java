
package ui;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.List;
import persistencia.AlunoRepository;
import aluno.Aluno;

public class PainelAluno extends JPanel {
    private static final Color AZUL_ESCURO_1 = Color.decode("#023373");
    private static final Color AZUL_ESCURO_2 = Color.decode("#023E73");
    private static final Color VERDE_1 = Color.decode("#02730A");
    private static final Color VERDE_2 = Color.decode("#055902");
    private static final Color BRANCO = Color.decode("#F2F2F2");

    private JTable tabelaAlunos;
    private AlunoTableModel tableModel;
    private List<Aluno> alunos;
    private AlunoRepository repo;
    private Runnable acaoVoltar;

    public PainelAluno(Runnable acaoVoltar) {
        this.acaoVoltar = acaoVoltar;
        setLayout(new BorderLayout());
        setBackground(BRANCO);

        repo = AlunoRepository.getInstance();
        alunos = repo.getAlunos();

        JPanel navbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navbar.setBackground(AZUL_ESCURO_1);
        JButton btnCadastrar = criarBotao("Cadastrar Aluno", VERDE_1, Color.WHITE);
        JButton btnEditar = criarBotao("Editar Selecionado", AZUL_ESCURO_2, Color.WHITE);
        JButton btnExcluir = criarBotao("Excluir Selecionado", VERDE_2, Color.WHITE);
        JButton btnVoltar = criarBotao("Voltar", AZUL_ESCURO_2, Color.WHITE);
        navbar.add(btnCadastrar);
        navbar.add(btnEditar);
        navbar.add(btnExcluir);
        navbar.add(btnVoltar);
        add(navbar, BorderLayout.NORTH);

        JPanel painelCentro = new JPanel(new BorderLayout());
        painelCentro.setBackground(BRANCO);
        JTextField campoBusca = new JTextField();
        campoBusca.setFont(new Font("SansSerif", Font.PLAIN, 16));
        campoBusca.setToolTipText("Buscar por nome, matrícula ou curso");
        painelCentro.add(campoBusca, BorderLayout.NORTH);

        tableModel = new AlunoTableModel(alunos);
        tabelaAlunos = new JTable((TableModel) tableModel);
        tabelaAlunos.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tabelaAlunos.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        tabelaAlunos.setRowHeight(28);
        JScrollPane scroll = new JScrollPane(tabelaAlunos);
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

        btnCadastrar.addActionListener(e -> cadastrarAluno());
        btnEditar.addActionListener(e -> editarAlunoSelecionado());
        btnExcluir.addActionListener(e -> excluirAlunoSelecionado());
        btnVoltar.addActionListener(e -> acaoVoltar.run());
    }

    private void cadastrarAluno() {
        JTextField campoNome = new JTextField();
        JTextField campoMatricula = new JTextField();
        JTextField campoCurso = new JTextField();
        String[] tipos = {"Normal", "Especial"};
        JComboBox<String> comboTipo = new JComboBox<>(tipos);

        JPanel painel = new JPanel(new GridLayout(0, 1));
        painel.add(new JLabel("Nome:"));
        painel.add(campoNome);
        painel.add(new JLabel("Matrícula:"));
        painel.add(campoMatricula);
        painel.add(new JLabel("Curso:"));
        painel.add(campoCurso);
        painel.add(new JLabel("Tipo:"));
        painel.add(comboTipo);

        int result = JOptionPane.showConfirmDialog(this, painel, "Cadastrar Aluno", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String nome = campoNome.getText().trim();
            String matricula = campoMatricula.getText().trim();
            String curso = campoCurso.getText().trim();
            String tipo = (String) comboTipo.getSelectedItem();
            if (nome.isEmpty() || matricula.isEmpty() || curso.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (repo.getAlunos().stream().anyMatch(a -> a.getMatricula().equalsIgnoreCase(matricula))) {
                JOptionPane.showMessageDialog(this, "A matrícula '" + matricula + "' já está em uso. Por favor, escolha outra.", "Matrícula Duplicada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            aluno.Aluno novoAluno;
            if ("Normal".equals(tipo)) {
                novoAluno = new aluno.AlunoNormal(nome, matricula, curso);
            } else {
                novoAluno = new aluno.AlunoEspecial(nome, matricula, curso);
            }
            repo.adicionarAluno(novoAluno);
            ((AbstractTableModel) tableModel).fireTableDataChanged();
            JOptionPane.showMessageDialog(this, "Aluno cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void excluirAlunoSelecionado() {
        int idx = tabelaAlunos.getSelectedRow();
        if (idx >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o aluno selecionado?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Aluno alunoParaExcluir = tableModel.getAlunoAt(idx);
                repo.removerAluno(alunoParaExcluir.getMatricula());
                ((AbstractTableModel) tableModel).fireTableDataChanged();
                JOptionPane.showMessageDialog(this, "Aluno excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um aluno para excluir.", "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editarAlunoSelecionado() {
        int idx = tabelaAlunos.getSelectedRow();
        if (idx >= 0) {
            Aluno aluno = tableModel.getAlunoAt(idx);
            JTextField campoNome = new JTextField(aluno.getNome());
            JTextField campoMatricula = new JTextField(aluno.getMatricula());
            campoMatricula.setEditable(false);
            JTextField campoCurso = new JTextField(aluno.getCurso());
            String[] tipos = {"Normal", "Especial"};
            JComboBox<String> comboTipo = new JComboBox<>(tipos);
            comboTipo.setSelectedItem(aluno.getTipo());

            JPanel painel = new JPanel(new GridLayout(0, 1));
            painel.add(new JLabel("Nome:"));
            painel.add(campoNome);
            painel.add(new JLabel("Matrícula:"));
            painel.add(campoMatricula);
            painel.add(new JLabel("Curso:"));
            painel.add(campoCurso);
            painel.add(new JLabel("Tipo:"));
            painel.add(comboTipo);

            int result = JOptionPane.showConfirmDialog(this, painel, "Editar Aluno", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String nome = campoNome.getText().trim();
                String curso = campoCurso.getText().trim();
                String tipo = (String) comboTipo.getSelectedItem();
                if (nome.isEmpty() || curso.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                aluno.setNome(nome);
                aluno.setCurso(curso);
                persistencia.ArquivoAluno.salvarAlunos(alunos);
                ((AbstractTableModel) tableModel).fireTableDataChanged();
                JOptionPane.showMessageDialog(this, "Aluno editado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um aluno para editar.", "Atenção", JOptionPane.WARNING_MESSAGE);
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

    private static class AlunoTableModel extends AbstractTableModel {
        private final String[] colunas = {"Nome", "Matrícula", "Curso"};
        private final List<Aluno> alunos;
        private String filtro = "";
        private List<Aluno> filtrados;

        public AlunoTableModel(List<Aluno> alunos) {
            this.alunos = alunos;
            this.filtrados = alunos;
        }

        public void setFiltro(String filtro) {
            this.filtro = filtro;
            if (filtro.isEmpty()) {
                filtrados = alunos;
            } else {
                filtrados = alunos.stream().filter(a ->
                    a.getNome().toLowerCase().contains(filtro) ||
                    a.getMatricula().toLowerCase().contains(filtro) ||
                    a.getCurso().toLowerCase().contains(filtro)
                ).toList();
            }
            fireTableDataChanged();
        }

        public Aluno getAlunoAt(int rowIndex) {
            return filtrados.get(rowIndex);
        }

        @Override
        public int getRowCount() {
            return filtrados.size();
        }

        @Override
        public int getColumnCount() {
            return colunas.length;
        }

        @Override
        public String getColumnName(int column) {
            return colunas[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Aluno aluno = filtrados.get(rowIndex);
            switch (columnIndex) {
                case 0: return aluno.getNome();
                case 1: return aluno.getMatricula();
                case 2: return aluno.getCurso();
                default: return "";
            }
        }
    }
}
