public class VendingItem{
    private String name;
    private double price = 0.00;
    private int quantity;
    private String type;
    private String slotIndicator;
    private int totalSales;
    // Add more fields as needed

    public VendingItem(String name, double price, String type) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.quantity = 5; // Initially stocked to the maximum amount
        this.totalSales = 0;// Initialize total sales to 0
    }

    // Implement getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSlotIndicator() {
        return slotIndicator;
    }

    public void setSlotIndicator(String slotIndicator) {
        this.slotIndicator = slotIndicator;
    }
    public int getTotalSales() {
        return totalSales;
    }

    // Method to increment total sales
    public void incrementTotalSales(int  quantitySold) {
        totalSales += quantitySold;
    }
}
