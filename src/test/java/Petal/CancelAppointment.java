package Petal;

import org.testng.annotations.Test;

import com.Petal.TestBase;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CancelAppointment extends TestBase {

    
    @Test(dependsOnGroups = "bookappointment")
    public void cancelappointment() {
    	
    	
        // Retrieve availabilityId from GetAvailability class
        String availabilityId = BookAppointment.appointment_reference_number;
        
        
        Response response = RestAssured.given()
        		.spec(petalSpec)
                .queryParam("patientId", GetAvailability.Patientid)
                .pathParam("appointment_reference_number", availabilityId)
                .contentType(ContentType.JSON)
                .when()
                .delete("/Appointments/booking_hub/appointments/{appointment_reference_number}/cancel_appointment");
        System.out.println("Status Code for cancel appointment endpoint is "+response.statusCode());
        response.then().statusCode(204);
    }
}