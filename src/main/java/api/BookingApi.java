package api;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.ConfigReader;

/**
 * API "page object": one method per endpoint. Tests call these methods and only do assertions.
 * Target: https://restful-booker.herokuapp.com
 */
public class BookingApi {

    private RequestSpecification spec() {
        return given()
                .baseUri(ConfigReader.get("api.base.url"))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .filter(new AllureRestAssured())   // attaches request + response to the Allure report
                .log().ifValidationFails();
    }

    @Step("POST /auth - create token")
    public Response createToken(String username, String password) {
        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);
        return spec().body(body).post("/auth");
    }

    public String getValidToken() {
        return createToken(ConfigReader.get("api.username"), ConfigReader.get("api.password"))
                .jsonPath().getString("token");
    }

    @Step("POST /booking - create booking")
    public Response createBooking(Booking booking) {
        return spec().body(booking).post("/booking");
    }

    @Step("GET /booking/{id}")
    public Response getBooking(int id) {
        return spec().get("/booking/" + id);
    }

    @Step("PUT /booking/{id} - update booking")
    public Response updateBooking(int id, Booking booking, String token) {
        return spec().cookie("token", token).body(booking).put("/booking/" + id);
    }

    @Step("DELETE /booking/{id}")
    public Response deleteBooking(int id, String token) {
        return spec().cookie("token", token).delete("/booking/" + id);
    }
}
