package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import static org.hamcrest.Matchers.lessThan;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import api.Booking;
import api.BookingApi;
import api.BookingDates;
import api.BookingResponse;
import io.restassured.response.Response;

/**
 * Full CRUD flow: create -> get -> update -> delete -> verify deleted.
 * Tests share bookingId, so they run in order using priority + dependsOnMethods.
 */
@Epic("Booking API")
@Feature("Booking CRUD")
public class BookingApiTest {

    private final BookingApi api = new BookingApi();
    private String token;
    private int bookingId;

    @BeforeClass
    public void getAuthToken() {
        token = api.getValidToken();
        assertNotNull(token, "Auth token should not be null");
    }

    @Test(priority = 1, groups = {"api", "smoke"})
    @Story("Create")
    @Severity(SeverityLevel.BLOCKER)
    public void createBooking() {
        Booking booking = new Booking("Anbu", "Tester", 1500, true,
                new BookingDates("2026-11-01", "2026-11-05"), "Breakfast");

        Response response = api.createBooking(booking);

        assertEquals(response.getStatusCode(), 200, "Create should return 200");
        response.then().time(lessThan(5000L));

        BookingResponse body = response.as(BookingResponse.class);
        bookingId = body.bookingid;
        assertTrue(bookingId > 0, "bookingid should be generated");
        assertEquals(body.booking.firstname, "Anbu");
        assertEquals(body.booking.totalprice, 1500);
    }

    @Test(priority = 2, dependsOnMethods = "createBooking", groups = {"api", "smoke"})
    @Story("Read")
    @Severity(SeverityLevel.CRITICAL)
    public void getBooking() {
        Response response = api.getBooking(bookingId);

        assertEquals(response.getStatusCode(), 200);
        Booking booking = response.as(Booking.class);
        assertEquals(booking.firstname, "Anbu");
        assertEquals(booking.lastname, "Tester");
        assertEquals(booking.bookingdates.checkin, "2026-11-01");
    }

    @Test(priority = 3, dependsOnMethods = "createBooking", groups = "api")
    @Story("Update")
    @Severity(SeverityLevel.CRITICAL)
    public void updateBooking() {
        Booking updated = new Booking("Anbu", "Updated", 2500, false,
                new BookingDates("2026-11-02", "2026-11-06"), "Dinner");

        Response response = api.updateBooking(bookingId, updated, token);

        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().getString("lastname"), "Updated");
        assertEquals(response.jsonPath().getInt("totalprice"), 2500);
    }

    @Test(priority = 4, dependsOnMethods = "createBooking", groups = "api")
    @Story("Delete")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteBooking() {
        Response response = api.deleteBooking(bookingId, token);
        // restful-booker returns 201 Created for a successful delete (known quirk of this API)
        assertEquals(response.getStatusCode(), 201);
    }

    @Test(priority = 5, dependsOnMethods = "deleteBooking", groups = "api")
    @Story("Delete")
    @Severity(SeverityLevel.NORMAL)
    public void getDeletedBookingReturns404() {
        assertEquals(api.getBooking(bookingId).getStatusCode(), 404);
    }

    @Test(groups = "api", description = "Negative: wrong credentials do not give a token")
    @Story("Authentication")
    @Severity(SeverityLevel.NORMAL)
    public void authWithWrongCredentials() {
        Response response = api.createToken("admin", "wrong_password");
        // API quirk: returns 200 with a 'reason' message instead of 401
        assertEquals(response.jsonPath().getString("reason"), "Bad credentials");
    }

    @Test(groups = "api", description = "Negative: update without token is forbidden")
    @Story("Authentication")
    @Severity(SeverityLevel.CRITICAL)
    public void updateWithoutTokenIsForbidden() {
        Booking b = new Booking("X", "Y", 100, true, new BookingDates("2026-12-01", "2026-12-02"), "None");
        Response response = api.updateBooking(1, b, "invalid-token");
        assertEquals(response.getStatusCode(), 403);
    }
}
