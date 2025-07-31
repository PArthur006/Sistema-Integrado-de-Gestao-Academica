package ui;

import disciplina.Disciplina;
import disciplina.Turma;
import persistencia.*;
import aluno.Aluno;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.Frame;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class PainelDisciplina extends JPanel {
    private List<Disciplina> disciplinas;
    private DisciplinaTableModel tableModel;
    private JTable tabelaDisciplinas;
    private JTextField campoBusca;
    private Runnable acaoVoltar;

    public PainelDisciplina(Runnable acaoVoltar) {
        this.acaoVoltar = acaoVoltar;
        setLayout(new BorderLayout());
        setBackground(UIUtils.BRANCO);

        disciplinas = DisciplinaRepository.getInstance().getDisciplinas();

        JPanel navbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navbar.setBackground(UIUtils.AZUL_ESCURO_1);
        JButton btnVoltar = UIUtils.criarBotao("⬅ Voltar", UIUtils.AZUL_ESCURO_2, Color.WHITE);
        JButton btnCadastrar = UIUtils.criarBotao("➕ Cadastrar Disciplina", UIUtils.VERDE_1, Color.WHITE);
        JButton btnEditar = UIUtils.criarBotao("✎ Editar Selecionada", UIUtils.AZUL_ESCURO_2, Color.WHITE);
        JButton btnExcluir = UIUtils.criarBotao("❌ Excluir Selecionada", UIUtils.VERDE_2, Color.WHITE);
        JButton btnTurmas = UIUtils.criarBotao("📚 Gerenciar Turmas", UIUtils.AZUL_ESCURO_1, Color.WHITE);
        navbar.add(btnVoltar);
        navbar.add(btnCadastrar);
        navbar.add(btnEditar);
        navbar.add(btnExcluir);
        navbar.add(btnTurmas);
        add(navbar, BorderLayout.NORTH);

        campoBusca = new JTextField();
        campoBusca.setFont(new Font("SansSerif", Font.PLAIN, 16));
        campoBusca.setToolTipText("Buscar por nome ou código");

        JPanel painelCentro = new JPanel(new BorderLayout());
        painelCentro.setBackground(UIUtils.BRANCO);
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
        String[] labels = {"Nome:", "Código:", "Carga Horária:", "Pré-requisitos (códigos, sep. por vírgula):"};
        FormularioDialog dialog = new FormularioDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cadastrar Disciplina", labels);
        dialog.setVisible(true);

        Map<String, String> valores = dialog.getValores();

        if (valores != null) {
            String nome = valores.get("Nome:");
            String codigo = valores.get("Código:");
            String cargaStr = valores.get("Carga Horária:");
            String preRequisitosStr = valores.get("Pré-requisitos (códigos, sep. por vírgula):");

            if (nome.isEmpty() || codigo.isEmpty() || cargaStr.isEmpty()) { // Validação de pré-requisitos não é obrigatória
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int carga;
            try {
                carga = Integer.parseInt(cargaStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Carga horária inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (disciplinas.stream().anyMatch(d -> d.getCodigo().equalsIgnoreCase(codigo))) {
                JOptionPane.showMessageDialog(this, "O código de disciplina '" + codigo + "' já existe. Por favor, escolha outro.", "Código Duplicado", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Disciplina nova = new Disciplina(nome, codigo, carga);
            if (preRequisitosStr != null && !preRequisitosStr.isEmpty()) {
                String[] preRequisitos = preRequisitosStr.split(",");
                for (String preReq : preRequisitos) {
                    nova.addPreRequisito(preReq.trim());
                }
            }
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
            String preRequisitosStr = String.join(", ", d.getPreRequisitos());
            String[] labels = {"Nome:", "Código:", "Carga Horária:", "Pré-requisitos (códigos, sep. por vírgula):"};
            String[] valoresIniciais = {d.getNome(), d.getCodigo(), String.valueOf(d.getCargaHoraria()), preRequisitosStr};

            FormularioDialog dialog = new FormularioDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Disciplina", labels, valoresIniciais);
            dialog.setCampoEditavel("Código:", false);
            dialog.setVisible(true);

            Map<String, String> valores = dialog.getValores();

            if (valores != null) {
                String nome = valores.get("Nome:");
                String cargaStr = valores.get("Carga Horária:");
                String novosPreRequisitosStr = valores.get("Pré-requisitos (códigos, sep. por vírgula):");

                if (nome.isEmpty() || cargaStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int carga;
                try {
                    carga = Integer.parseInt(cargaStr);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Carga horária inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                for (Disciplina disc : disciplinas) {
                    if (disc.getCodigo().equals(d.getCodigo())) {
                        disc.setNome(nome);
                        disc.setCargaHoraria(carga);
                        // Atualiza os pré-requisitos
                        disc.getPreRequisitos().clear();
                        if (novosPreRequisitosStr != null && !novosPreRequisitosStr.isEmpty()) {
                            for (String preReq : novosPreRequisitosStr.split(",")) {
                                disc.addPreRequisito(preReq.trim());
                            }
                        }
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
