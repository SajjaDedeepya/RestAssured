package Petal;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.Petal.TestBase;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class BookAppointment extends TestBase {

    public static String appointment_reference_number;

    
    
    @Test(dependsOnGroups = "lockAvailability" , groups= "bookappointment")
    public void bookappointment() {
    	
    	
        // Retrieve availabilityId from GetAvailability class
        int bookavailabilityId = LockAvailability.lockavailabilityid;
        System.out.println(bookavailabilityId);

        // Prepare the request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("availabilityId", bookavailabilityId);
        requestBody.put("patientId", GetAvailability.Patientid); // Example patient ID
        requestBody.put("contextId", null);
        requestBody.put("visitReason", "General Checkup");
        requestBody.put("contactPhoneNumber", "7989013720");
        requestBody.put("communicationPreference", "sms");
        requestBody.put("reminderEmail", "patient@example.com");
        requestBody.put("reminderSmsNumber", "7989013720");
        requestBody.put("reminderPhoneNumber", "7989013720");
        requestBody.put("locale", "en");
        requestBody.put("serviceTypeId", 9);
        
        
        Response response = RestAssured.given()
        		.spec(petalSpec)
                .body(requestBody)
                .contentType(ContentType.JSON)
                .when()
                .post("/Appointments/booking_hub/appointments/create_appointment");
        System.out.println("Status Code for book appointment endpoint is "+response.statusCode());
        response.then().statusCode(201);
        appointment_reference_number= response.jsonPath().getString("referenceNumber");
        System.out.println(appointment_reference_number);


    }
}

