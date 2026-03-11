import java.util.*;

class InventoryManager {

    // productId -> stock count
    private HashMap<String, Integer> stock = new HashMap<>();

    // productId -> waiting list (FIFO)
    private LinkedHashMap<String, Queue<Integer>> waitingList = new LinkedHashMap<>();

    // Add product to inventory
    public void addProduct(String productId, int quantity) {
        stock.put(productId, quantity);
        waitingList.put(productId, new LinkedList<>());
    }

    // Check stock availability
    public int checkStock(String productId) {
        return stock.getOrDefault(productId, 0);
    }

    // Purchase item (thread-safe)
    public synchronized String purchaseItem(String productId, int userId) {

        int currentStock = stock.getOrDefault(productId, 0);

        if (currentStock > 0) {
            stock.put(productId, currentStock - 1);
            return "Success, " + (currentStock - 1) + " units remaining";
        } else {
            Queue<Integer> queue = waitingList.get(productId);
            queue.add(userId);
            int position = queue.size();
            return "Added to waiting list, position #" + position;
        }
    }

    // Display waiting list
    public void showWaitingList(String productId) {
        System.out.println("Waiting List: " + waitingList.get(productId));
    }
}

public class FlashSaleInventoryManagerApp {

    public static void main(String[] args) {

        InventoryManager manager = new InventoryManager();

        // Add product with limited stock
        manager.addProduct("IPHONE15_256GB", 100);

        System.out.println("Flash Sale Inventory Manager\n");

        // Check stock
        System.out.println("checkStock(\"IPHONE15_256GB\") → "
                + manager.checkStock("IPHONE15_256GB") + " units available");

        // Simulate purchases
        System.out.println("purchaseItem(\"IPHONE15_256GB\", userId=12345) → "
                + manager.purchaseItem("IPHONE15_256GB", 12345));

        System.out.println("purchaseItem(\"IPHONE15_256GB\", userId=67890) → "
                + manager.purchaseItem("IPHONE15_256GB", 67890));

        // Simulate stock running out
        for (int i = 0; i < 100; i++) {
            manager.purchaseItem("IPHONE15_256GB", 10000 + i);
        }

        // After stock ends
        System.out.println("purchaseItem(\"IPHONE15_256GB\", userId=99999) → "
                + manager.purchaseItem("IPHONE15_256GB", 99999));

        manager.showWaitingList("IPHONE15_256GB");
    }
}