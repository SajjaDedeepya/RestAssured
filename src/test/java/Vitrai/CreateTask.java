package Vitrai;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.Petal.TestBase;


public class CreateTask extends TestBase {
	
	public static String questionnaireid,linkid,taskid;
	

	
	 @Test(groups= "task")
	 public void createtask() {
		 
		// Prepare the request body
	        Map<String, Object> requestBody = new HashMap<>();
	        requestBody.put("languageCode", "en");
	        requestBody.put("patientIdentifier", "Alice");
	        requestBody.put("chiefComplaint", "Kidney Pain");
	        requestBody.put("hasConsentAITraining", true);
	        requestBody.put("identifier", "https://www.ramq.gouv.qc.ca/|55101");
	        requestBody.put("type", "FMC");
	    		        
	        Response response = RestAssured.given()
	        		.spec(vitraiSpec)
	                .body(requestBody)
	                .contentType(ContentType.JSON)
	                .when()
	                .post("/CreateTask/create_task");
	        System.out.println("Status Code for create task endpoint is "+response.statusCode());
	        response.then().statusCode(200);
	        if(response.statusCode() != 200) {
	            System.out.println(response.getBody().asString());}
	        
	        JsonPath jsonPath = response.jsonPath();
	        taskid=jsonPath.getString("taskId");
	        questionnaireid=jsonPath.getString("resultantQuestionnaire.questionnaireResponseId");
	        linkid=jsonPath.getString("resultantQuestionnaire.item[0].linkId");
	        System.out.println(" taskid: "+taskid+" questionnaireid: "+questionnaireid+" linkid: "+linkid);

	    }

}
