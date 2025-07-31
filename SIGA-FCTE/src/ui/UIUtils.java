package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Classe utilitária para centralizar constantes e métodos comuns da UI.
 */
public final class UIUtils {

    // Paleta de Cores Padrão
    public static final Color AZUL_ESCURO_1 = Color.decode("#023373");
    public static final Color AZUL_ESCURO_2 = Color.decode("#023E73");
    public static final Color VERDE_1 = Color.decode("#02730A");
    public static final Color VERDE_2 = Color.decode("#055902");
    public static final Color BRANCO = Color.decode("#F2F2F2");

    // Construtor privado para impedir a instanciação.
    private UIUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    public static JButton criarBotao(String texto, Color corFundo, Color corTexto) {
        JButton botao = new JButton(texto);
        botao.setBackground(corFundo);
        botao.setForeground(corTexto);
        botao.setFocusPainted(false);
        botao.setFont(new Font("SansSerif", Font.BOLD, 16));
        botao.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return botao;
    }
}