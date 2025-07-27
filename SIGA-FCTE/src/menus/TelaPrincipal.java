package menus;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    // Cores usadas na interface
    private static final Color AZUL_ESCURO_1 = Color.decode("#023373");
    private static final Color AZUL_ESCURO_2 = Color.decode("#023E73");
    private static final Color VERDE_1 = Color.decode("#02730A");
    private static final Color VERDE_2 = Color.decode("#055902");
    private static final Color BRANCO = Color.decode("#F2F2F2");

    public TelaPrincipal() {
        setTitle("SIGA-FCTE - Tela Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setContentPane(criarPainelPrincipal());
    }

    private JPanel criarPainelDeMenu(String tituloDoMenu) {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(BRANCO);

        // Navbar (painel superior)
        JPanel navbar = new JPanel();
        navbar.setBackground(AZUL_ESCURO_1);
        navbar.setPreferredSize(new Dimension(600, 40));
        painel.add(navbar, BorderLayout.NORTH);

        // Título central
        JLabel titulo = new JLabel(tituloDoMenu);
        titulo.setForeground(AZUL_ESCURO_2);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setHorizontalAlignment(SwingConstants.CENTER); 
        painel.add(titulo, BorderLayout.CENTER);

        // Rodapé com botão de voltar
        JButton btnVoltar = criarBotao("Voltar", VERDE_1, BRANCO);
        btnVoltar.addActionListener(e -> trocarPainel(criarPainelPrincipal()));

        JPanel rodape = new JPanel();
        rodape.setBackground(BRANCO);
        rodape.add(btnVoltar);
        painel.add(rodape, BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarPainelPrincipal() {
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        
        // Header
        JPanel header = new JPanel();
        header.setBackground(AZUL_ESCURO_1);
        JLabel titulo = new JLabel("SIGA-FCTE");
        titulo.setForeground(BRANCO);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        header.add(titulo);
        painelPrincipal.add(header, BorderLayout.NORTH);

        // Painel central com os botões de menu
        JPanel painelCentral = new JPanel(new GridBagLayout());
        painelCentral.setBackground(BRANCO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton btnAlunos = criarBotao("Alunos", AZUL_ESCURO_2, BRANCO);
        JButton btnDisciplinas = criarBotao("Disciplinas", VERDE_1, BRANCO);
        JButton btnAvaliacoes = criarBotao("Avaliações", VERDE_2, BRANCO);
        JButton btnSair = criarBotao("Sair", AZUL_ESCURO_1, BRANCO);

        gbc.gridy = 0; painelCentral.add(btnAlunos, gbc);
        gbc.gridy = 1; painelCentral.add(btnDisciplinas, gbc);
        gbc.gridy = 2; painelCentral.add(btnAvaliacoes, gbc);
        gbc.gridy = 3; painelCentral.add(btnSair, gbc);

        // Ações dos botões para trocar os painéis
        btnAlunos.addActionListener(e -> trocarPainel(criarPainelDeMenu("Menu de Alunos")));
        btnDisciplinas.addActionListener(e -> trocarPainel(criarPainelDeMenu("Menu de Disciplinas")));
        btnAvaliacoes.addActionListener(e -> trocarPainel(criarPainelDeMenu("Menu de Avaliações")));
        btnSair.addActionListener(e -> System.exit(0));

        painelPrincipal.add(painelCentral, BorderLayout.CENTER);
        return painelPrincipal;
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
    
    private void trocarPainel(JPanel novoPainel) {
        setContentPane(novoPainel);
        revalidate(); 
        repaint(); 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
    }
}