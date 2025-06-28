package Vitrai;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.Petal.TestBase;

public class GetServiceRequest extends TestBase {
	

	public static int availabilityid;
	
	@Test(dependsOnGroups=  "questionnaire", groups="service request")
	 public void getservicerequest() {
		
		Map<String, Object> request = new HashMap<>();
        request.put("hin", "PATG85101219");
        request.put("hinSequence", "1");
        request.put("firstName", "Gamma");
        request.put("lastName", "Patient");
        request.put("birthDate", "1985-10-12");
        request.put("gender", "male");
        request.put("serviceTypeId", 9);
        request.put("postalCode", "G8Z1X3");
        request.put("range", 10);
        request.put("page",1);
        request.put("taskId", CreateTask.taskid);
        
        System.out.println("task ID: " + CreateTask.taskid);
		
		Response response = RestAssured.given()
				    .spec(vitraiSpec)
	                .body(request)
	                .contentType(ContentType.JSON)
	                .when()
	                .post("/Service/get_service_request");
		System.out.println("Status Code for get service request endpoint is "+response.statusCode());
        response.then().statusCode(200);
        if(response.statusCode() != 200) {
            System.out.println(response.getBody().asString());}
	       
	     // Extract availabilityQueryResponses safely
	        JsonPath jsonPath = response.jsonPath();
	        availabilityid = jsonPath.getInt("availabilityQueryResponses[0].id");
	        System.out.println("Availability ID: " + availabilityid);
	    }

}
