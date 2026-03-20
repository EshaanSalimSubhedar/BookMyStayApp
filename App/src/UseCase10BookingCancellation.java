import java.util.*;

/**
 * ============================================================
 * CLASS - CancellationService
 * ============================================================
 *
 * @version 10.0
 */
class CancellationService {

    // Stack to track released room IDs (LIFO rollback)
    private Stack<String> cancellationStack = new Stack<>();

    /**
     * Cancels a booking and restores inventory
     */
    public void cancelBooking(
            String reservationId,
            Map<String, Set<String>> allocatedRooms,
            RoomInventory inventory) {

        // Determine room type from ID
        String roomType = reservationId.split("-")[0] + " Room";

        // Validate existence
        if (!allocatedRooms.containsKey(roomType) ||
                !allocatedRooms.get(roomType).contains(reservationId)) {

            System.out.println("Cancellation failed: Invalid reservation ID");
            return;
        }

        // Remove allocation
        allocatedRooms.get(roomType).remove(reservationId);

        // Push to rollback stack
        cancellationStack.push(reservationId);

        // Restore inventory
        Map<String, Integer> availability = inventory.getRoomAvailability();
        availability.put(roomType, availability.get(roomType) + 1);

        System.out.println("Booking cancelled successfully. Inventory restored for room type: "
                + roomType.split(" ")[0]);
    }

    /**
     * Displays rollback stack
     */
    public void showRollbackHistory() {
        System.out.println("Rollback History (Most Recent First): " + cancellationStack);
    }
}

/**
 * ============================================================
 * MAIN CLASS - UseCase10BookingCancellation
 * ============================================================
 *
 * @version 10.0
 */
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        System.out.println("Booking Cancellation\n");

        // Inventory
        RoomInventory inventory = new RoomInventory();

        // Simulated allocated rooms (from UC6)
        Map<String, Set<String>> allocatedRooms = new HashMap<>();

        allocatedRooms.put("Single Room", new HashSet<>(Arrays.asList("Single-1")));
        allocatedRooms.put("Double Room", new HashSet<>(Arrays.asList("Double-1")));
        allocatedRooms.put("Suite Room", new HashSet<>(Arrays.asList("Suite-1")));

        // Cancellation service
        CancellationService service = new CancellationService();

        // Perform cancellation
        String reservationId = "Single-1";

        service.cancelBooking(reservationId, allocatedRooms, inventory);

        // Show rollback history
        service.showRollbackHistory();

        // Show updated inventory
        System.out.println("Updated Single Room availability: "
                + inventory.getRoomAvailability().get("Single Room"));
    }
}