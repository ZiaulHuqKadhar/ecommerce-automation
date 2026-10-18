package api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Response of POST /booking: { "bookingid": 123, "booking": { ... } } */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingResponse {
    public int bookingid;
    public Booking booking;
}
