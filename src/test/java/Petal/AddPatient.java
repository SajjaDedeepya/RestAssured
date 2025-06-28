package Petal;

import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.Petal.TestBase;
import com.Petal.jsonreader;
import com.Petal.patientdatareader;
import com.fasterxml.jackson.core.type.TypeReference;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class AddPatient extends TestBase {
	
    private static List<patientdatareader> requestDataList;

    @DataProvider(name = "patientdatareader")
    public static Object[][] patiendatareader() {
        try {
            requestDataList = jsonreader.readJsonFile("com/Petal/Addrequest.json", 
                new TypeReference<List<patientdatareader>>(){});
            System.out.println("Successfully loaded " + requestDataList.size() + " patient requests.");
        } catch (Exception e) {
            System.err.println("Failed to load patient requests from JSON: " + e.getMessage());
            requestDataList = List.of(); // Default to empty list to avoid null
        }
        return requestDataList.stream()
                .map(request -> new Object[]{request})
                .toArray(Object[][]::new);
    }
    @Test(dataProvider = "patientdatareader")
    public void testFindPatient(patientdatareader request) {
        System.out.println("Executing test for hin: " + request.getHin());

        Response response = RestAssured.given()
        		.spec(petalSpec)
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .post("/Patients/booking_hub/patients/add_patient");
        System.out.println("Status Code for Add Patient endpoint with "+request.getHin()+" is "+response.statusCode());
        if(response.statusCode() != 200 || response.statusCode() != 201) {
            System.out.println(response.getBody().asString());}


    }

}
