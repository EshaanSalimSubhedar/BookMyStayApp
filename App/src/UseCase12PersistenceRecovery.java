import java.io.*;
import java.util.*;

/**
 * ============================================================
 * CLASS - FilePersistenceService
 * ============================================================
 *
 * @version 12.0
 */
class FilePersistenceService {

    private static final String FILE_NAME = "inventory_data.txt";

    /**
     * Saves inventory data to file
     */
    public void saveInventory(Map<String, Integer> inventory) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }

            System.out.println("Inventory saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    /**
     * Loads inventory data from file
     */
    public Map<String, Integer> loadInventory() {

        Map<String, Integer> inventory = new HashMap<>();

        File file = new File(FILE_NAME);

        // Handle missing file
        if (!file.exists()) {
            System.out.println("No saved inventory found. Starting fresh.");
            return inventory;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");

                inventory.put(parts[0], Integer.parseInt(parts[1]));
            }

            System.out.println("Inventory loaded successfully.");

        } catch (IOException e) {
            System.out.println("Error loading inventory: " + e.getMessage());
        }

        return inventory;
    }
}

/**
 * ============================================================
 * MAIN CLASS - UseCase12PersistenceRecovery
 * ============================================================
 *
 * @version 12.0
 */
public class UseCase12PersistenceRecovery {

    public static void main(String[] args) {

        System.out.println("System Recovery\n");

        FilePersistenceService persistenceService = new FilePersistenceService();

        // Load previous state
        Map<String, Integer> inventory = persistenceService.loadInventory();

        // If empty → initialize default
        if (inventory.isEmpty()) {
            inventory.put("Single Room", 5);
            inventory.put("Double Room", 3);
            inventory.put("Suite Room", 2);
        }

        // Show current state
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Simulate update
        inventory.put("Single Room", inventory.get("Single Room") - 1);

        // Save updated state
        persistenceService.saveInventory(inventory);
    }
}