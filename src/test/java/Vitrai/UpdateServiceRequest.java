package Vitrai;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.Petal.TestBase;

public class UpdateServiceRequest extends TestBase {
	
	

	
	@Test(dependsOnGroups=  "service request")
	 public void updateservicerequest() {
		
		Map<String, Object> request = new HashMap<>();
        request.put("availabilityId", GetServiceRequest.availabilityid);
        request.put("patientId", 192);
        request.put("contextId", "");
        request.put("visitReason", "Kidney Pain");
        request.put("contactPhoneNumber", "7989013720");
        request.put("communicationPreference", "phone");
        request.put("reminderEmail", "sajja@sante.quebec");
        request.put("reminderSmsNumber", "7989013720");
        request.put("reminderPhoneNumber", "7989013720");
        request.put("locale","en");
        request.put("serviceTypeId", 9);
        request.put("confirm","true");
        request.put("time","");
        request.put("lockTime",null);
        request.put("flag",0);
        request.put("lockStatus",0);
        request.put("taskId", CreateTask.taskid);
        request.put("isNotOHP","true");
		
		Response response = RestAssured.given()
				    .spec(vitraiSpec)
	                .body(request)
	                .contentType(ContentType.JSON)
	                .when()
	                .post("/Service/get_service_request");
		System.out.println("Status Code for update service request endpoint is "+response.statusCode());
	        response.then().statusCode(200);
	        if(response.statusCode() != 200) {
	            System.out.println(response.getBody().asString());}
	       

	    }

}
