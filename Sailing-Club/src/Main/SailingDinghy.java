package Main;

import java.util.ArrayList;
import java.util.List;

public class SailingDinghy {
    private final String name; // Name of the sailing dinghy
    private final List<Booking> bookings = new ArrayList<>(); // List to store bookings for the dinghy

    // Constructor to initialize the dinghy's name
    public SailingDinghy(String name) {
        this.name = name;
    }

    // Getter method for the dinghy's name
    public String getName() {
        return name;
    }

    // Getter method for the list of bookings
    public List<Booking> getBookings() {
        return bookings;
    }

    // Method to add a booking to the list
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }
}
