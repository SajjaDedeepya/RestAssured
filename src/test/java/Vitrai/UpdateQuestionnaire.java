package Vitrai;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.Petal.TestBase;

public class UpdateQuestionnaire extends TestBase { 
	
	String linkid;
	
    Response response;
    Map<String, Object> requestBody = new HashMap<>();

    @Test(dependsOnGroups = "task", groups= "questionnaire")
    public void updatequestionnaire() {
    	
        // Construct the initial questionnaire request
        JSONObject questionnaireRequest = new JSONObject();
        questionnaireRequest.put("questionnaireResponseId", CreateTask.questionnaireid);
        questionnaireRequest.put("answerOption", "Yes");
        questionnaireRequest.put("linkId", CreateTask.linkid);
        questionnaireRequest.put("rollbackFlag", false);
        questionnaireRequest.put("questionNumber", 0);
        questionnaireRequest.put("rollbackAnswerOption", "asd");

        // Send the initial POST request and log the response
        Response initialResponse = RestAssured.given()
        		.spec(vitraiSpec)
                .contentType(ContentType.JSON)
                .body(questionnaireRequest.toString())
                .post("/QuestionarireResponse/patch_update_questionnaire");
        
        linkid=initialResponse.jsonPath().getString("questionnaireResponseObj.data.item[0].linkId");
        // Check if the response status code is 200
        if (initialResponse.getStatusCode() != 200) {
            Assert.fail("Initial POST request failed. Status Code: " + initialResponse.getStatusCode() + ", Response: " + initialResponse.asString());
        }

        // Check if the response is valid JSON
        String responseBody = initialResponse.asString();
        if (responseBody == null || responseBody.trim().isEmpty()) {
            Assert.fail("Initial response body is empty. Status Code: " + initialResponse.getStatusCode());
        }

        // Check Content-Type
        String contentType = initialResponse.getContentType();
        if (!contentType.contains("application/json")) {
            Assert.fail("Initial response is not JSON. Content-Type: " + contentType + ", Response: " + responseBody);
        }

        // Check status code and handle errors
        String responseStatusCode;
        try {
            responseStatusCode = initialResponse.jsonPath().getString("questionnaireResponseObj.statusCode");
        } catch (Exception e) {
            Assert.fail("Failed to parse initial response as JSON. Response: " + responseBody + ", Error: " + e.getMessage());
            return;
        }

        if (!"OK".equals(responseStatusCode)) {
            String errorMessage = initialResponse.jsonPath().getString("questionnaireResponseObj.errorResponse.issue[0].diagnostics");
            Assert.fail("Initial API returned an error. StatusCode: " + responseStatusCode + ", Error: " + errorMessage + ", Response: " + responseBody);
        }

        // Extract status from the response
        String status;
        try {
            status = initialResponse.jsonPath().getString("questionnaireResponseObj.data.status");
        } catch (Exception e) {
            Assert.fail("Failed to extract status from initial response. Response: " + responseBody + ", Error: " + e.getMessage());
            return;
        }

        if (status == null) {
            Assert.fail("Initial status is null in the response. Response: " + responseBody);
        }

        // If already completed, no need to proceed
        if ("completed".equals(status)) {
            return;
        }

        // Get already answered linkIds
        List<String> answeredLinkIds;
        try {
            answeredLinkIds = initialResponse.jsonPath().getList("questionnaireResponseObj.data.item.linkId");
        } catch (Exception e) {
            Assert.fail("Failed to extract answered linkIds from initial response. Response: " + responseBody + ", Error: " + e.getMessage());
            return;
        }

        if (answeredLinkIds == null) {
            answeredLinkIds = new java.util.ArrayList<>();
        }

        // Log the answered linkIds for debugging
        System.out.println("Answered LinkIds: " + answeredLinkIds);

        // Check if the initial linkId from CreateTask is already answered (with null check)
        if (linkid != null && answeredLinkIds.stream().anyMatch(answered -> answered.equalsIgnoreCase(linkid))) {
            // Use questionnaireRequestObj to get the next unanswered linkId
            List<String> allLinkIds;
            try {
                allLinkIds = initialResponse.jsonPath().getList("questionnaireRequestObj.data.item.linkId");
            } catch (Exception e) {
                Assert.fail("Failed to extract questionnaire items from questionnaireRequestObj in initial response. Response: " + responseBody + ", Error: " + e.getMessage());
                return;
            }

            if (allLinkIds == null) {
                Assert.fail("Could not retrieve questionnaire items from questionnaireRequestObj in initial response. Response: " + responseBody);
            }

            // Log all linkIds for debugging
            //System.out.println("All LinkIds: " + allLinkIds);

            // Find the first unanswered linkId (case-insensitive comparison)
            linkid = null;
            for (String potentialLinkId : allLinkIds) {
                if (!answeredLinkIds.stream().anyMatch(answered -> answered.equalsIgnoreCase(potentialLinkId))) {
                    linkid = potentialLinkId;
                    break;
                }
            }

            if (linkid == null) {
                Assert.fail("No unanswered questions found. All questions are already answered. Response: " + responseBody);
            }
        }

        int questionNumber = answeredLinkIds.size();

        // Loop until status is "completed"
        while (!"completed".equals(status)) {
            // Ensure linkid is not null before proceeding
            if (linkid == null) {
                Assert.fail("LinkId is null before sending the next POST request. Answered LinkIds: " + answeredLinkIds);
            }

            // Construct questionnaire request
            JSONObject questionnaireRequest1 = new JSONObject();
            questionnaireRequest1.put("questionnaireResponseId", CreateTask.questionnaireid);
            questionnaireRequest1.put("answerOption", "No");
            questionnaireRequest1.put("linkId", linkid);
            questionnaireRequest1.put("rollbackFlag", false);
            questionnaireRequest1.put("questionNumber", questionNumber);
            questionnaireRequest1.put("rollbackAnswerOption", "");

            // Send POST request to update questionnaire (use the same endpoint)
            Response questionnaireResponse = RestAssured.given()
            		.spec(vitraiSpec)
                    .contentType(ContentType.JSON)
                    .body(questionnaireRequest1.toString())
                    .post("/QuestionarireResponse/patch_update_questionnaire");

            // Check if the response status code is 200
            if (questionnaireResponse.getStatusCode() != 200) {
                Assert.fail("POST request failed. Status Code: " + questionnaireResponse.getStatusCode() + ", Response: " + questionnaireResponse.asString());
            }

            // Check if the response is valid JSON
            responseBody = questionnaireResponse.asString();
            if (responseBody == null || responseBody.trim().isEmpty()) {
                Assert.fail("Response body is empty. Status Code: " + questionnaireResponse.getStatusCode());
            }

            // Check Content-Type
            contentType = questionnaireResponse.getContentType();
            if (!contentType.contains("application/json")) {
                Assert.fail("Response is not JSON. Content-Type: " + contentType + ", Response: " + responseBody);
            }

            // Check status code and handle errors
            try {
                responseStatusCode = questionnaireResponse.jsonPath().getString("questionnaireResponseObj.statusCode");
            } catch (Exception e) {
                Assert.fail("Failed to parse response as JSON. Response: " + responseBody + ", Error: " + e.getMessage());
                return;
            }

            if (!"OK".equals(responseStatusCode)) {
                String errorMessage = questionnaireResponse.jsonPath().getString("questionnaireResponseObj.errorResponse.issue[0].diagnostics");
                Assert.fail("API returned an error. StatusCode: " + responseStatusCode + ", Error: " + errorMessage + ", Response: " + responseBody);
            }

            // Extract status from the response
            try {
                status = questionnaireResponse.jsonPath().getString("questionnaireResponseObj.data.status");
            } catch (Exception e) {
                Assert.fail("Failed to extract status from response. Response: " + responseBody + ", Error: " + e.getMessage());
                return;
            }

            if (status == null) {
                Assert.fail("Status is null in the response. Response: " + responseBody);
            }

            // If status is not completed, find the next unanswered linkId
            if (!"completed".equals(status)) {
                // Update answered linkIds
                try {
                    answeredLinkIds = questionnaireResponse.jsonPath().getList("questionnaireResponseObj.data.item.linkId");
                } catch (Exception e) {
                    Assert.fail("Failed to extract answered linkIds. Response: " + responseBody + ", Error: " + e.getMessage());
                    return;
                }

                if (answeredLinkIds == null) {
                    Assert.fail("Could not retrieve answered linkIds. Response: " + responseBody);
                }

                // Log the answered linkIds for debugging
               // System.out.println("Answered LinkIds: " + answeredLinkIds);

                // Get all linkIds from questionnaireRequestObj
                List<String> allLinkIds;
                try {
                    allLinkIds = questionnaireResponse.jsonPath().getList("questionnaireRequestObj.data.item.linkId");
                } catch (Exception e) {
                    Assert.fail("Failed to extract questionnaire items from questionnaireRequestObj. Response: " + responseBody + ", Error: " + e.getMessage());
                    return;
                }

                if (allLinkIds == null) {
                    Assert.fail("Could not retrieve questionnaire items from questionnaireRequestObj. Response: " + responseBody);
                }

                // Log all linkIds for debugging
                //System.out.println("All LinkIds: " + allLinkIds);

                // Find the first unanswered linkId (case-insensitive comparison)
                linkid = null;
                for (String potentialLinkId : allLinkIds) {
                    if (!answeredLinkIds.stream().anyMatch(answered -> answered.equalsIgnoreCase(potentialLinkId))) {
                        linkid = potentialLinkId;
                        break;
                    }
                }

                if (linkid == null) {
                    Assert.fail("No unanswered linkId found in the response. Answered LinkIds: " + answeredLinkIds + ", All LinkIds: " + allLinkIds + ", Response: " + responseBody);
                }
                questionNumber++;
            }
        }
    }
}