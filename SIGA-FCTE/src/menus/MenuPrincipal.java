package menus;

import java.util.Scanner;

public class MenuPrincipal {
    public static void exibir(Scanner scanner) {
        while (true) {
            System.out.println("\nMenu Principal");
            System.out.println("1. Menu Aluno");
            System.out.println("2. Menu Disciplina");
            System.out.println("3. Menu Avaliação");
            System.out.println("4. Sair");
            System.out.print("Escolha uma opção: ");
            
            try {
                int opcao = Integer.parseInt(scanner.nextLine());
                
                switch (opcao) {
                    case 1:
                        MenuAluno.exibir(scanner);
                        break;
                    case 2:
                        MenuDisciplina.exibir(scanner);
                        break;
                    case 3:
                        MenuAvaliacao.exibir(scanner);
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido!");
            }
        }
    }
}