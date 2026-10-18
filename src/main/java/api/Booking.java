package api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** POJO = request/response body. Jackson converts it to/from JSON automatically. */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Booking {
    public String firstname;
    public String lastname;
    public int totalprice;
    public boolean depositpaid;
    public BookingDates bookingdates;
    public String additionalneeds;

    public Booking() { }

    public Booking(String firstname, String lastname, int totalprice, boolean depositpaid,
                   BookingDates bookingdates, String additionalneeds) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.totalprice = totalprice;
        this.depositpaid = depositpaid;
        this.bookingdates = bookingdates;
        this.additionalneeds = additionalneeds;
    }
}
