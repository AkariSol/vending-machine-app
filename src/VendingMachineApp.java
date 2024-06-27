import java.util.Scanner;

public class VendingMachineApp {
    public static void main(String[] args) {
        VendingMachine vendingMachine = new VendingMachine("vendingmachine.csv");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayMainMenu();


            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    vendingMachine.displayItems();

                    break;
                case 2:
                    vendingMachine.purchaseMenu(scanner);
                    break;
                case 3:
                    vendingMachine.exit();
                    return;
                case 4:
                    vendingMachine.generateSalesReport();
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
    }
}
    private static void displayMainMenu() {
        System.out.println("MAIN MENU");
        String[] mainMenu = {"(1) Display Vending Machine items", "(2) Purchase", "(3) Exit"};
        for (String menuOptions : mainMenu) {
            System.out.println(menuOptions);
        }
        System.out.println("Choose option number");
    }

}