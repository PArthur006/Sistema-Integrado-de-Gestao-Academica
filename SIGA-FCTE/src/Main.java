import ui.TelaPrincipal;
import persistencia.ArquivoPrincipal;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new TelaPrincipal().setVisible(true);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ArquivoPrincipal.salvarTudo();
        }));
    }
}
