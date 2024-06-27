import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class VendingMachine{
    private Map<String, VendingItem> items;
    private double totalSales = 0.00;
    private double currentMoney = 0.00;
    private String selectedProducts;

    // Add more fields as needed

    public VendingMachine(String inputFile) {
        items = new HashMap<>();
        // Initialize vending machine items from the input file
        loadItemsFromFile(inputFile);


    }

    public void loadItemsFromFile(String inputFile) {
        try (Scanner scanner = new Scanner(new File(String.valueOf(inputFile)))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    String slotLocation = parts[0];
                    String productName = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    String type = parts[3];
                    items.put(slotLocation, new VendingItem(productName, price, type));
                } else {
                    System.err.println("Invalid input line: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Input file not found: " + inputFile);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing price: " + e.getMessage());
        }
    }

    public void displayItems() {
        // Display all items in the vending machine
        System.out.println("\nVending Machine Items:");
        for (Map.Entry<String, VendingItem> entry : items.entrySet()) {
            String slotLocation = entry.getKey();
            VendingItem item = entry.getValue();
            System.out.println(slotLocation + ": " + item.getName() + " $" + item.getPrice() + " Quantity: " + item.getQuantity());
        }
    }

    public void purchaseMenu(Scanner scanner) {
        // Implement purchase menu
        while (true){
            displaySecondMenu();
            int choice = scanner.nextInt();
            switch (choice){
                case 1:
                    currentMoney = feedMoney(scanner,currentMoney);
                    break;
                case 2:
                    selectProduct(scanner);
                    break;
                case 3:
                    finishTransaction();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");

            }
        }


    }
    // Add a method to update total sales for all items
    private void updateTotalSales() {
        for (Map.Entry<String, VendingItem> entry : items.entrySet()) {
            VendingItem item = entry.getValue();
            totalSales += item.getTotalSales() * item.getPrice();
        }
    }
    public void generateSalesReport() {
        // Generate sales report
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        String timestamp = now.format(formatter);
        String fileName = "Sales_Report_" + timestamp + ".txt";

        try (PrintWriter dataOutput = new PrintWriter(fileName)) {
            dataOutput.println("Sales Report");

            // Iterate through items and write sales for each product
            for (Map.Entry<String, VendingItem> entry : items.entrySet()) {
                VendingItem item = entry.getValue();
                int sales = entry.getValue().getTotalSales();
                dataOutput.println(item.getName() + "|" + item.getTotalSales());
            }

            // Calculate and write total sales
            updateTotalSales();//Ensure total sales is updated

            dataOutput.println("\nTOTAL SALES $" + String.format("%.2f", totalSales));
        } catch (FileNotFoundException e) {
            System.err.println("Error generating sales report: " + e.getMessage());
        }
        System.out.println("Sales report generated: " + fileName);
    }

    private  void displaySecondMenu() {
        String[] secondaryMenu = {"(1) Feed Money", "(2) Select Product", "(3) Finish Transaction"};
        for (String menuOptions : secondaryMenu) {
            System.out.println(menuOptions);
        }
        System.out.println("Choose option number");
    }
    private double feedMoney(Scanner scanner, double currentMoney) {
        System.out.print("Enter amount to feed (whole dollar amounts only): ");
        int amount = scanner.nextInt();
        currentMoney += amount;
        System.out.printf("Current Money Provided: $%.2f%n", currentMoney);
        // Log transaction
        logTransaction("FEED MONEY", amount, currentMoney,null,null);
        return currentMoney;
    }

    private void selectProduct(Scanner scanner) {
        // Implement product selection logic

        for (Map.Entry<String, VendingItem> entry : items.entrySet()) {
            String slotLocation = entry.getKey();
            VendingItem item = entry.getValue();
            System.out.println(slotLocation + ": " + item.getName() + " $" + item.getPrice() + " Quantity: " + item.getQuantity());
        }
        System.out.println("Enter slot code of the product you want to purchase (or type 'cancel' to cancel):");


        String slotCode = scanner.next();
        if (slotCode.equalsIgnoreCase("cancel")) {
            System.out.printf("Transaction canceled. Returning remaining money: $%.2f%n", currentMoney);
            exit();
            return;
        }
        if (!items.containsKey(slotCode.toUpperCase())) {
            System.out.println("Invalid slot code. Please try again.");
            return;
        }

        VendingItem selectedItem = items.get(slotCode.toUpperCase());

        if (selectedItem.getQuantity() <= 0) {
            System.out.println("Sorry, the selected item is sold out.");
            return;
        }

        if (currentMoney < selectedItem.getPrice()) {
            System.out.println("Insufficient funds. Please add more money.");
            return;
        }

        // Dispense the product
        dispenseProduct(selectedItem);

        // Increment total sales for the selected item
        selectedItem.incrementTotalSales(1);


        // Log transaction
        logTransaction("PURCHASE", selectedItem.getPrice(), currentMoney, selectedItem.getName(), slotCode.toUpperCase());

    }
    private void dispenseProduct(VendingItem item) {
        // Decrease quantity of the item
        item.setQuantity(item.getQuantity() - 1);

        // Update money remaining
        currentMoney -= item.getPrice();
        System.out.printf("Money remaining: $%.2f%n", currentMoney);

        // Print item details
        System.out.println("Dispensing: " + item.getName() + " $" + item.getPrice());

        // Print specific message based on item type
        switch (item.getType()) {
            case "Chip":
                System.out.println("Crunch Crunch, Yum!");
                break;
            case "Candy":
                System.out.println("Munch Munch, Yum!");
                break;
            case "Drink":
                System.out.println("Glug Glug, Yum!");
                break;
            case "Gum":
                System.out.println("Chew Chew, Yum!");
                break;
        }
    }

    private void logTransaction(String transactionType, double amount, double newBalance, String productName, String slotLocation) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
        String timestamp = now.format(formatter);

        // Format transaction based on transaction type
        String logEntry;
        switch (transactionType) {
            case "FEED MONEY":
                logEntry = String.format("%s %s: $%.2f $%.2f", timestamp, transactionType, amount, newBalance);
                break;
            case "GIVE CHANGE":
                logEntry = String.format("%s %s: $%.2f $%.2f", timestamp, transactionType, amount, newBalance);
                break;
            default:
                logEntry = String.format("%s %s %s %s $%.2f $%.2f", timestamp, productName, slotLocation, transactionType, amount, newBalance);
        }

        try (PrintWriter dataOutput = new PrintWriter(new FileOutputStream(new File("Log.txt"), true))) {
            dataOutput.println(logEntry);
        } catch (FileNotFoundException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    private void finishTransaction() {
        // Implement finishing transaction logic
        int remainingChange = (int) (currentMoney * 100); // Convert remaining money to cents
        int quarters = remainingChange / 25;
        remainingChange %= 25;
        int dimes = remainingChange / 10;
        remainingChange %= 10;
        int nickels = remainingChange / 5;

        logTransaction("GIVE CHANGE", currentMoney, 0.00,null,null);

        currentMoney = 0.00;

        // Display the change in coins
        StringBuilder changeBuilder = new StringBuilder("Dispensing remaining change: ");
        if (quarters > 0){
            changeBuilder.append(quarters + ":Quarters ");
        }
        if (dimes >0){
            changeBuilder.append(dimes + ":Dimes ");
        }
        if (nickels >0){
            changeBuilder.append(nickels + ":Nickels");
        }
        System.out.println(changeBuilder);

    }
    public void exit() {
        // Cleanup and exit
        System.out.println("Thanks for using Vendo - Matic 800");
        System.exit(0);
    }
}
