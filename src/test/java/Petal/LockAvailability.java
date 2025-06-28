package Petal;

import org.testng.annotations.Test;

import com.Petal.TestBase;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class LockAvailability extends TestBase {

    public static int lockavailabilityid; 

    
    
    @Test(dependsOnGroups = "getAvailability", groups = "lockAvailability")
    public void testPatchLockAvailability() {
        // Retrieve availabilityId from GetAvailability class
    	lockavailabilityid = GetAvailability.availabilityId;

        // Ensure availabilityId is not null
        if (lockavailabilityid == 0) {
            throw new AssertionError("Availability ID is not available from GET request");
        }
        
        Response response = RestAssured.given()
        		.spec(petalSpec)
                .header("Content-Type", "application/json")
                .queryParam("PatientId", GetAvailability.Patientid)
                .pathParam("availability_id",lockavailabilityid)
                .when()
                .patch("/Availability/booking_hub/availabilities/{availability_id}/lock_availabilities");
        System.out.println("Status Code for lock availability endpoint is "+response.statusCode());
        System.out.println(response.getBody().asString());
        response.then().statusCode(200);

    }
}
