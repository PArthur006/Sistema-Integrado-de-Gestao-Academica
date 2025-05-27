import menus.MenuPrincipal;
import persistencia.ArquivoPrincipal;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            MenuPrincipal.exibir(scanner);
        } finally {
            ArquivoPrincipal.salvarTudo();
            scanner.close();
        }
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
