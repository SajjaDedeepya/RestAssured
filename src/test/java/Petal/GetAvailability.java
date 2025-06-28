package Petal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.testng.annotations.Test;

import com.Petal.TestBase;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


public class GetAvailability extends TestBase {

    public static int availabilityId;
    public static int Patientid= 1466;
    
    //Test Case-01
    @Test(groups = "getAvailability")
    public void Availabilitywithfamilydoctor() {
     

        Response response = RestAssured.given()
        		.spec(petalSpec)
                .header("Content-Type", "application/json")
                .queryParam("PatientId", Patientid)
                .queryParam("ServiceTypeId", 9)
                .queryParam("FamilyDoctor", true)
                .queryParam("InGmf", true)
                .when()
                .get("/Availability/booking_hub/availabilities/get_availabilities");
        response.then().statusCode(200);
        System.out.println("Status Code for get availability endpoint for familydoctor is "+response.statusCode());
        
     // Parse the JSON response
        JsonPath jsonPath = response.jsonPath();

        // Calculate the date 3 days from today
        LocalDate today = LocalDate.now(); // Hard coded for April 18, 2025
        LocalDate targetDate = today.plusDays(1); // April 18, 2025, if we want, we can filter availabilities based on date
        String targetDateString = targetDate.format(DateTimeFormatter.ISO_LOCAL_DATE); // Format: 2025-04-21
        
     // Filter the JSON array to find objects where startTime begins with the target date
        List<Integer> matchingIds = jsonPath.getList(
                "findAll { it.startTime.startsWith('" + targetDateString + "') }.id"
        );
        
        availabilityId = matchingIds.get(0);
        System.out.println(availabilityId);


    }
    @Test(groups = "Availability")
  //Test Case-02
    public void AvailabilitywithClinic() {
     

        Response response = RestAssured.given()
        		.spec(petalSpec)
                .header("Content-Type", "application/json")
                .queryParam("PatientId", Patientid)
                .queryParam("ServiceTypeId", 9)
                .queryParam("FamilyDoctor", false)
                .queryParam("InGmf", true)
                .when()
                .get("/Availability/booking_hub/availabilities/get_availabilities");
        System.out.println("Status Code for get availability endpoint for clinic is "+response.statusCode());
        if(response.statusCode() != 200) {
            System.out.println(response.getBody().asString());}


    }
    
    @Test(groups = "Availability")
  //Test Case-03
    public void AvailabilityforOrphan() {
     

        Response response = RestAssured.given()
        		.spec(petalSpec)
                .header("Content-Type", "application/json")
                .queryParam("PatientId", 122)
                .queryParam("ServiceTypeId", 9)
                .queryParam("FamilyDoctor", false)
                .queryParam("InGmf", false)
                .queryParam("PostalCode", "G8Z1X3")
                .queryParam("Range", 20)
                .when()
                .get("/Availability/booking_hub/availabilities/get_availabilities");
        response.then().statusCode(200);
        System.out.println("Status Code for get availability endpoint for orphan is "+response.statusCode());
        if(response.statusCode() != 200) {
            System.out.println(response.getBody().asString());}


    }
}
