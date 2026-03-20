import java.util.*;

/**
 * ============================================================
 * CLASS - BookingHistory
 * ============================================================
 *
 * @version 8.0
 */
class BookingHistory {

    private List<Reservation> confirmedReservations;

    public BookingHistory() {
        confirmedReservations = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        confirmedReservations.add(reservation);
    }

    public List<Reservation> getConfirmedReservations() {
        return confirmedReservations;
    }
}

/**
 * ============================================================
 * CLASS - BookingReportService
 * ============================================================
 *
 * @version 8.0
 */
class BookingReportService {

    public void generateReport(BookingHistory history) {

        System.out.println("Booking History Report\n");

        for (Reservation r : history.getConfirmedReservations()) {

            System.out.println("Guest: "
                    + r.getGuestName()
                    + ", Room Type: "
                    + r.getRoomType());
        }
    }
}

/**
 * ============================================================
 * MAIN CLASS - UseCase8BookingHistoryReport
 * ============================================================
 *
 * @version 8.0
 */
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();

        // Simulating confirmed bookings (from UC6)
        history.addReservation(new Reservation("Abhi", "Single"));
        history.addReservation(new Reservation("Subha", "Double"));
        history.addReservation(new Reservation("Vanmathi", "Suite"));

        // Generate report
        BookingReportService reportService = new BookingReportService();
        reportService.generateReport(history);
    }
}