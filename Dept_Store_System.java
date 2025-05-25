import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class Product {
    private String name;
    private String tag;
    private String itemCode;
    private String description;
    private double price;
    private int quantity;

    public Product(String name, String tag, String itemCode, String description, double price, int quantity) {
        this.name = name;
        this.tag = tag;
        this.itemCode = itemCode;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() { return name; }
    public String getItemCode() { return itemCode; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int qty) { this.quantity = qty; }

    @Override
    public String toString() {
        return name + " (" + itemCode + ") - $" + price + " | In stock: " + quantity;
    }
}
class InventoryManager {
    private Map<String, Product> productMap = new HashMap<>();

    public void addProduct(Product product) {
        productMap.put(product.getItemCode(), product);
    }

    public Product getProduct(String itemCode) {
        return productMap.get(itemCode);
    }

    public void updateStock(String itemCode, int quantitySold) {
        Product p = productMap.get(itemCode);
        if (p != null && p.getQuantity() >= quantitySold) {
            p.setQuantity(p.getQuantity() - quantitySold);
        }
    }

    public void listInventory() {
        if (productMap.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            for (Product p : productMap.values()) {
                System.out.println(p);
            }
        }
    }

    public boolean itemExists(String itemCode) {
        return productMap.containsKey(itemCode);
    }
}
class SalesTransaction {
    private List<Product> items = new ArrayList<>();
    private Map<Product, Integer> quantityMap = new HashMap<>();
    private double discountRate = 0.0;
    private String paymentMethod;

    public void scanItem(Product product, int quantity) {
        items.add(product);
        quantityMap.put(product, quantity);
    }

    public void applyDiscount(String event) {
        switch (event.toLowerCase()) {
            case "1": 
                discountRate = 0.0;
                break;
            case "2": 
                discountRate = 0.10;
                break;
            case "3": 
                discountRate = 0.15;
                break;
            case "4": 
                discountRate = 0.20;
                break;
            default:
                discountRate = 0.0;
        }
    }

    public void setPaymentMethod(String method) {
        this.paymentMethod = method;
    }

    public double calculateTotal() {
        double total = 0.0;
        for (Product product : items) {
            total += product.getPrice() * quantityMap.get(product);
        }
        return total - (total * discountRate);
    }

    public void printBill() {
        System.out.println("\n======= BILL =======");
        for (Product product : items) {
            int qty = quantityMap.get(product);
            System.out.printf("%s x%d @ $%.2f = $%.2f\n",
                    product.getName(), qty, product.getPrice(), qty * product.getPrice());
        }
        System.out.printf("Discount Applied: %.2f%%\n", discountRate * 100);
        System.out.printf("Total Amount: $%.2f\n", calculateTotal());
        System.out.println("Payment Method: " + paymentMethod);
        System.out.println("====================");
    }
}
public class Dept_Store_System {
    private static Scanner scanner = new Scanner(System.in);
    private static InventoryManager inventoryManager = new InventoryManager();

    public static void main(String[] args) {
        seedProducts();
        boolean running = true;

        while (running) {
            System.out.println("\n==== DEPARTMENT STORE SYSTEM ====");
            System.out.println("1. View Inventory");
            System.out.println("2. Add New Product");
            System.out.println("3. New Sale");
            System.out.println("4. Exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    inventoryManager.listInventory();
                    break;
                case "2":
                    addNewProduct();
                    break;
                case "3":
                    handleSales();
                    break;
                case "4":
                    running = false;
                    System.out.println("Exiting system. Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void seedProducts() {
        inventoryManager.addProduct(new Product("Shampoo", "TAG01", "001", "Hair Care", 5.50, 50));
        inventoryManager.addProduct(new Product("Toothpaste", "TAG02", "002", "Oral Care", 2.75, 30));
        inventoryManager.addProduct(new Product("Notebook", "TAG03", "003", "Stationery", 1.50, 100));
    }

    private static void addNewProduct() {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter tag: ");
        String tag = scanner.nextLine();
        System.out.print("Enter item code: ");
        String code = scanner.nextLine();

        if (inventoryManager.itemExists(code)) {
            System.out.println("Item code already exists!");
            return;
        }

        System.out.print("Enter description: ");
        String desc = scanner.nextLine();
        System.out.print("Enter price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter quantity: ");
        int qty = Integer.parseInt(scanner.nextLine());

        Product newProduct = new Product(name, tag, code, desc, price, qty);
        inventoryManager.addProduct(newProduct);
        System.out.println("Product added successfully.");
    }

    private static void handleSales() {
        SalesTransaction transaction = new SalesTransaction();
        while (true) {
            System.out.print("Enter Item Code (or 'done'): ");
            String code = scanner.nextLine();
            if (code.equalsIgnoreCase("done")) break;

            Product product = inventoryManager.getProduct(code);
            if (product == null) {
                System.out.println("Invalid item code.");
                continue;
            }

            System.out.print("Quantity: ");
            int qty = Integer.parseInt(scanner.nextLine());

            if (product.getQuantity() < qty) {
                System.out.println("Not enough stock.");
                continue;
            }

            transaction.scanItem(product, qty);
            inventoryManager.updateStock(code, qty);
        }

        System.out.println("Select Discount Option:");
        System.out.println("1. No Discount");
        System.out.println("2. New Year (10%)");
        System.out.println("3. Holi Offer (15%)");
        System.out.println("4. Diwali Carnival Offer (20%)");
        System.out.print("Enter option number: ");
        String discountChoice = scanner.nextLine();
        transaction.applyDiscount(discountChoice);

        System.out.print("Payment Method (Cash/Card/UPI): ");
        String method = scanner.nextLine();
        transaction.setPaymentMethod(method);
        scanner.close();


        transaction.printBill();
    }
}
