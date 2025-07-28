package menus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class PainelListarAlunos extends JPanel {
    private static final Color AZUL_ESCURO_1 = Color.decode("#023373");
    private static final Color AZUL_ESCURO_2 = Color.decode("#023E73");
    private static final Color VERDE_1 = Color.decode("#02730A");
    private static final Color VERDE_2 = Color.decode("#055902");
    private static final Color BRANCO = Color.decode("#F2F2F2");

    private DefaultListModel<String> listaModel;
    private JList<String> listaAlunos;
    private List<String> alunos;
    private Runnable acaoVoltar;

    public PainelListarAlunos(List<String> alunos, Runnable acaoVoltar, ActionListener acaoExcluir) {
        this.alunos = alunos;
        this.acaoVoltar = acaoVoltar;
        setLayout(new BorderLayout());
        setBackground(BRANCO);

        // Navbar
        JPanel navbar = new JPanel();
        navbar.setBackground(AZUL_ESCURO_1);
        navbar.setPreferredSize(new Dimension(600, 40));
        add(navbar, BorderLayout.NORTH);

        // Título
        JLabel titulo = new JLabel("Lista de Alunos");
        titulo.setForeground(AZUL_ESCURO_2);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(titulo, BorderLayout.CENTER);

        // Painel de lista
        JPanel painelLista = new JPanel();
        painelLista.setBackground(BRANCO);
        painelLista.setLayout(new BoxLayout(painelLista, BoxLayout.Y_AXIS));

        for (String aluno : alunos) {
            JPanel linha = new JPanel(new BorderLayout());
            linha.setBackground(BRANCO);
            JLabel lblAluno = new JLabel(aluno);
            lblAluno.setFont(new Font("SansSerif", Font.PLAIN, 18));
            JButton btnExcluir = criarBotao("Excluir", VERDE_2, BRANCO);
            btnExcluir.setActionCommand(aluno);
            btnExcluir.addActionListener(acaoExcluir);
            linha.add(lblAluno, BorderLayout.CENTER);
            linha.add(btnExcluir, BorderLayout.EAST);
            painelLista.add(linha);
        }

        JScrollPane scroll = new JScrollPane(painelLista);
        scroll.setPreferredSize(new Dimension(400, 200));
        add(scroll, BorderLayout.SOUTH);

        // Rodapé com botão voltar
        JButton btnVoltar = criarBotao("Voltar", AZUL_ESCURO_1, BRANCO);
        btnVoltar.addActionListener(e -> acaoVoltar.run());
        JPanel rodape = new JPanel();
        rodape.setBackground(BRANCO);
        rodape.add(btnVoltar);
        add(rodape, BorderLayout.PAGE_END);
    }

    private JButton criarBotao(String texto, Color corFundo, Color corTexto) {
        JButton botao = new JButton(texto);
        botao.setBackground(corFundo);
        botao.setForeground(corTexto);
        botao.setFocusPainted(false);
        botao.setFont(new Font("SansSerif", Font.BOLD, 16));
        botao.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        return botao;
    }
}
