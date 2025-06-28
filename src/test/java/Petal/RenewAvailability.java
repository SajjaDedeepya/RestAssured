package Petal;

import org.testng.annotations.Test;

import com.Petal.TestBase;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class RenewAvailability extends TestBase {


    
    
    @Test(dependsOnGroups = "lockAvailability")
    public void renewAvailability() {
        // Retrieve availabilityId from GetAvailability class
        int availabilityId = LockAvailability.lockavailabilityid;

        // Ensure availabilityId is not null
        if (availabilityId == 0) {
            throw new AssertionError("Availability ID is not available from GET request");
        }
        
        Response response = RestAssured.given()
        		.spec(petalSpec)
                .header("Content-Type", "application/json")
                .queryParam("patient_id", GetAvailability.Patientid)
                .pathParam("availability_id",availabilityId)
                .when()
                .patch("/Availability/booking_hub/availabilities/{availability_id}/renew_reservation");
        response.then().statusCode(200)
        .extract().response(); // Extract response even if status is 200
        // Print response body or status code regardless of success
        System.out.println("Status Code for renew availability endpoint is "+response.statusCode());
        if(response.statusCode() != 200 || response.statusCode() != 201) {
            System.out.println(response.getBody().asString());}


    }
}
