package menus;

import javax.swing.*;
import java.awt.*;

public class TelaAvaliacao extends JFrame {
    private static final Color AZUL_ESCURO_1 = Color.decode("#023373");
    private static final Color AZUL_ESCURO_2 = Color.decode("#023E73");
    private static final Color VERDE_2 = Color.decode("#055902");
    private static final Color BRANCO = Color.decode("#F2F2F2");

    public TelaAvaliacao() {
        setTitle("SIGA-FCTE - Avaliações");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setContentPane(criarPainelAvaliacao());
    }

    private JPanel criarPainelAvaliacao() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(BRANCO);

        // Navbar
        JPanel navbar = new JPanel();
        navbar.setBackground(AZUL_ESCURO_1);
        navbar.setPreferredSize(new Dimension(500, 40));
        painel.add(navbar, BorderLayout.NORTH);

        // Título centralizado
        JLabel titulo = new JLabel("Menu de Avaliações");
        titulo.setForeground(AZUL_ESCURO_2);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        painel.add(titulo, BorderLayout.CENTER);

        // Rodapé com botão voltar
        JButton btnVoltar = criarBotao("Voltar", VERDE_2, BRANCO);
        btnVoltar.addActionListener(e -> dispose());
        JPanel rodape = new JPanel();
        rodape.setBackground(BRANCO);
        rodape.add(btnVoltar);
        painel.add(rodape, BorderLayout.SOUTH);

        return painel;
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
