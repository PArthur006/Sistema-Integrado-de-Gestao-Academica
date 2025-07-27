import menus.TelaPrincipal;
import persistencia.ArquivoPrincipal;

public class Main {
    public static void main(String[] args) {
        // Inicia a interface gráfica principal
        javax.swing.SwingUtilities.invokeLater(() -> {
            new TelaPrincipal().setVisible(true);
        });

        // Salva tudo ao fechar o programa
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ArquivoPrincipal.salvarTudo();
        }));
    }

    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("Não foi possível limpar o console: " + e.getMessage());
        }
    }
}
