import java.util.*;

/**
 * ============================================================
 * MAIN CLASS - UseCase6RoomAllocationService
 * ============================================================
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * @version 6.0
 */
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        System.out.println("Room Allocation Processing\n");

        // Queue from UC5
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        bookingQueue.addRequest(new Reservation("Abhi", "Single Room"));
        bookingQueue.addRequest(new Reservation("Subha", "Double Room"));
        bookingQueue.addRequest(new Reservation("Vanmathi", "Suite Room"));

        // Inventory from UC3
        RoomInventory inventory = new RoomInventory();
        Map<String, Integer> availability = inventory.getRoomAvailability();

        // Allocation tracking
        Map<String, Set<String>> allocatedRooms = new HashMap<>();

        allocatedRooms.put("Single Room", new HashSet<>());
        allocatedRooms.put("Double Room", new HashSet<>());
        allocatedRooms.put("Suite Room", new HashSet<>());

        // Room counters for ID generation
        Map<String, Integer> counters = new HashMap<>();
        counters.put("Single Room", 1);
        counters.put("Double Room", 1);
        counters.put("Suite Room", 1);

        // Process queue (FIFO)
        while (bookingQueue.hasPendingRequests()) {

            Reservation request = bookingQueue.getNextRequest();
            String roomType = request.getRoomType();

            int available = availability.get(roomType);

            if (available > 0) {

                // Generate unique room ID
                int count = counters.get(roomType);
                String roomId = roomType.split(" ")[0] + "-" + count;

                // Ensure uniqueness (Set)
                if (!allocatedRooms.get(roomType).contains(roomId)) {

                    allocatedRooms.get(roomType).add(roomId);

                    // Update inventory
                    availability.put(roomType, available - 1);

                    // Increment counter
                    counters.put(roomType, count + 1);

                    System.out.println("Booking confirmed for Guest: "
                            + request.getGuestName()
                            + ", Room ID: "
                            + roomId);
                }

            } else {
                System.out.println("No rooms available for Guest: "
                        + request.getGuestName());
            }
        }
    }
}