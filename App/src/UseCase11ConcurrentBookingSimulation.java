import java.util.*;

/**
 * ============================================================
 * CLASS - ConcurrentBookingProcessor
 * ============================================================
 *
 * @version 11.0
 */
class ConcurrentBookingProcessor implements Runnable {

    private BookingRequestQueue bookingQueue;
    private RoomInventory inventory;
    private Map<String, Set<String>> allocatedRooms;

    public ConcurrentBookingProcessor(
            BookingRequestQueue bookingQueue,
            RoomInventory inventory,
            Map<String, Set<String>> allocatedRooms) {

        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.allocatedRooms = allocatedRooms;
    }

    @Override
    public void run() {

        while (true) {

            Reservation request;

            // Synchronize queue access
            synchronized (bookingQueue) {

                if (!bookingQueue.hasPendingRequests()) {
                    break;
                }

                request = bookingQueue.getNextRequest();
            }

            processBooking(request);
        }
    }

    private void processBooking(Reservation request) {

        String roomType = request.getRoomType();

        // Synchronize inventory (critical section)
        synchronized (inventory) {

            Map<String, Integer> availability = inventory.getRoomAvailability();

            if (availability.get(roomType) > 0) {

                String roomId = roomType.split(" ")[0] + "-"
                        + (allocatedRooms.get(roomType).size() + 1);

                allocatedRooms.get(roomType).add(roomId);

                availability.put(roomType, availability.get(roomType) - 1);

                System.out.println("Booking confirmed for Guest: "
                        + request.getGuestName()
                        + ", Room ID: " + roomId);

            } else {
                System.out.println("No rooms available for Guest: "
                        + request.getGuestName());
            }
        }
    }
}

/**
 * ============================================================
 * MAIN CLASS - UseCase11ConcurrentBookingSimulation
 * ============================================================
 *
 * @version 11.0
 */
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        System.out.println("Concurrent Booking Simulation\n");

        // Shared queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        bookingQueue.addRequest(new Reservation("Abhi", "Single Room"));
        bookingQueue.addRequest(new Reservation("Subha", "Single Room"));
        bookingQueue.addRequest(new Reservation("Karan", "Single Room"));

        // Shared inventory
        RoomInventory inventory = new RoomInventory();

        // Shared allocation map
        Map<String, Set<String>> allocatedRooms = new HashMap<>();
        allocatedRooms.put("Single Room", new HashSet<>());

        // Two threads
        Thread t1 = new Thread(
                new ConcurrentBookingProcessor(bookingQueue, inventory, allocatedRooms)
        );

        Thread t2 = new Thread(
                new ConcurrentBookingProcessor(bookingQueue, inventory, allocatedRooms)
        );

        // Start threads
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted");
        }

        // Final state
        System.out.println("\nRemaining inventory: "
                + inventory.getRoomAvailability().get("Single Room"));
    }
}