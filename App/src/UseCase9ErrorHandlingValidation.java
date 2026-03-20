import java.util.*;

/**
 * ============================================================
 * CLASS - InvalidBookingException
 * ============================================================
 */
class InvalidBookingException extends Exception {

    public InvalidBookingException(String message) {
        super(message);
    }
}

/**
 * ============================================================
 * CLASS - BookingValidator
 * ============================================================
 */
class BookingValidator {

    private static final Set<String> validRoomTypes = new HashSet<>(
            Arrays.asList("Single", "Double", "Suite")
    );

    public static void validateRoomType(String roomType) throws InvalidBookingException {

        if (!validRoomTypes.contains(roomType)) {
            throw new InvalidBookingException("Invalid room type selected.");
        }
    }

    public static void validateAvailability(RoomInventory inventory, String roomType)
            throws InvalidBookingException {

        Map<String, Integer> availability = inventory.getRoomAvailability();

        String key = roomType + " Room";

        if (!availability.containsKey(key) || availability.get(key) <= 0) {
            throw new InvalidBookingException("No rooms available for selected type.");
        }
    }
}

/**
 * ============================================================
 * MAIN CLASS - UseCase9ErrorHandlingValidation
 * ============================================================
 */
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        System.out.println("Booking Validation\n");

        Scanner scanner = new Scanner(System.in);

        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        try {
            System.out.print("Enter guest name: ");
            String name = scanner.nextLine();

            System.out.print("Enter room type (Single/Double/Suite): ");
            String roomType = scanner.nextLine();

            // Validation
            BookingValidator.validateRoomType(roomType);
            BookingValidator.validateAvailability(inventory, roomType);

            // If valid → add to queue
            bookingQueue.addRequest(new Reservation(name, roomType + " Room"));

            System.out.println("Booking request validated and added.");

        } catch (InvalidBookingException e) {

            System.out.println("Booking failed: " + e.getMessage());

        } finally {
            scanner.close();
        }
    }
}