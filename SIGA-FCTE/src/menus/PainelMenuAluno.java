package menus;

import javax.swing.*;
import java.awt.*;

public class PainelMenuAluno extends JPanel {
    private static final Color AZUL_ESCURO_1 = Color.decode("#023373");
    private static final Color AZUL_ESCURO_2 = Color.decode("#023E73");
    private static final Color VERDE_1 = Color.decode("#02730A");
    private static final Color VERDE_2 = Color.decode("#055902");
    private static final Color BRANCO = Color.decode("#F2F2F2");

    public PainelMenuAluno(Runnable acaoVoltar, Runnable acaoCadastrar, Runnable acaoListar) {
        setLayout(new BorderLayout());
        setBackground(BRANCO);

        // Navbar
        JPanel navbar = new JPanel();
        navbar.setBackground(AZUL_ESCURO_1);
        navbar.setPreferredSize(new Dimension(600, 40));
        add(navbar, BorderLayout.NORTH);

        // Título
        JLabel titulo = new JLabel("Menu de Alunos");
        titulo.setForeground(AZUL_ESCURO_2);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(titulo, BorderLayout.CENTER);

        // Botões
        JPanel painelBotoes = new JPanel(new GridBagLayout());
        painelBotoes.setBackground(BRANCO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton btnCadastrar = criarBotao("Cadastrar Aluno", VERDE_1, BRANCO);
        JButton btnListar = criarBotao("Listar Alunos", VERDE_2, BRANCO);
        JButton btnVoltar = criarBotao("Voltar", AZUL_ESCURO_1, BRANCO);

        btnCadastrar.addActionListener(e -> acaoCadastrar.run());
        btnListar.addActionListener(e -> acaoListar.run());
        btnVoltar.addActionListener(e -> acaoVoltar.run());

        gbc.gridy = 0; painelBotoes.add(btnCadastrar, gbc);
        gbc.gridy = 1; painelBotoes.add(btnListar, gbc);
        gbc.gridy = 2; painelBotoes.add(btnVoltar, gbc);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    private JButton criarBotao(String texto, Color corFundo, Color corTexto) {
        JButton botao = new JButton(texto);
        botao.setBackground(corFundo);
        botao.setForeground(corTexto);
        botao.setFocusPainted(false);
        botao.setFont(new Font("SansSerif", Font.BOLD, 18));
        botao.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        return botao;
    }
}

