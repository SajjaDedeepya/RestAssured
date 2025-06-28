package Petal;

import org.testng.annotations.Test;

import com.Petal.TestBase;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class RefuseAvailability extends TestBase {

    public static int refuseavailabilityId;
    
    
    @Test(dependsOnGroups = "lockAvailability")
    public void refuseAvailability() {
    	
    	String request="{ \"refusal_reason\": \"not interested\" }";
    	
        // Retrieve availabilityId from GetAvailability class
        refuseavailabilityId = LockAvailability.lockavailabilityid;
        System.out.println(refuseavailabilityId);

        // Ensure availabilityId is not null
        /*if (availabilityId == 0) {
            throw new AssertionError("Availability ID is not available from GET request");
        }*/
       
        Response response = RestAssured.given()
        		.spec(petalSpec)
                .queryParam("patient_id", GetAvailability.Patientid)
                .pathParam("availability_id",refuseavailabilityId)
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .patch("/Availability/booking_hub/availabilities/{availability_id}/refuse_availabilities");
        response.then().statusCode(200)
        .extract().response(); // Extract response even if status is 200
        // Print response body or status code regardless of success
        System.out.println("Status Code for refuse availability endpoint is "+response.statusCode());
        if(response.statusCode() != 200 || response.statusCode() != 201) {
            System.out.println(response.getBody().asString());}


    }
}