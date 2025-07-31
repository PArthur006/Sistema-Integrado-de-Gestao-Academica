package ui;

import javax.swing.*;

import java.awt.*;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        setTitle("SIGA-FCTE - Tela Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setContentPane(criarPainelPrincipal());
    }

    private JPanel criarPainelPrincipal() {
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        
        // Header
        JPanel header = new JPanel();
        header.setBackground(UIUtils.AZUL_ESCURO_1);
        header.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel titulo = new JLabel("SIGA-FCTE");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        header.add(titulo);
        
        painelPrincipal.add(header, BorderLayout.NORTH);

        // Painel central com os botões de menu
        JPanel painelCentral = new JPanel(new GridBagLayout());
        painelCentral.setBackground(UIUtils.BRANCO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton btnAlunos = UIUtils.criarBotao("🧑‍🎓 Alunos", UIUtils.AZUL_ESCURO_2, Color.WHITE);
        JButton btnDisciplinas = UIUtils.criarBotao("📚 Disciplinas", UIUtils.VERDE_1, Color.WHITE);
        JButton btnAvaliacoes = UIUtils.criarBotao("📝 Avaliações", UIUtils.VERDE_2, Color.WHITE);
        JButton btnSair = UIUtils.criarBotao("🚪 Sair", UIUtils.AZUL_ESCURO_1, Color.WHITE);

        gbc.gridy = 0; painelCentral.add(btnAlunos, gbc);
        gbc.gridy = 1; painelCentral.add(btnDisciplinas, gbc);
        gbc.gridy = 2; painelCentral.add(btnAvaliacoes, gbc);
        gbc.gridy = 3; painelCentral.add(btnSair, gbc);

        // Ações dos botões para trocar os painéis
        btnAlunos.addActionListener(e -> {
            JPanel painelAluno = new PainelAluno(() -> trocarPainel(criarPainelPrincipal()));
            trocarPainel(painelAluno);
        });
        btnDisciplinas.addActionListener(e -> {
            JPanel painelDisciplina = new PainelDisciplina(() -> trocarPainel(criarPainelPrincipal()));
            trocarPainel(painelDisciplina);
        });
        btnAvaliacoes.addActionListener(e -> {
            JPanel painelAvaliacao = new PainelAvaliacao(() -> trocarPainel(criarPainelPrincipal()));
            trocarPainel(painelAvaliacao);
        });
        btnSair.addActionListener(e -> System.exit(0));

        painelPrincipal.add(painelCentral, BorderLayout.CENTER);
        return painelPrincipal;
    }

    private void trocarPainel(JPanel novoPainel) {
        setContentPane(novoPainel);
        revalidate(); 
        repaint(); 
    }
}