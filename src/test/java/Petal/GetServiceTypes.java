package Petal;

import org.testng.annotations.Test;

import com.Petal.TestBase;

import io.restassured.RestAssured;

public class GetServiceTypes extends TestBase {


    @Test
    public void testApi() {
    	
    	RestAssured.given()
        		.spec(petalSpec)
                .get("/ServiceTypes/booking_hub/service_types/get_service")
                .then().statusCode(200);
        
    }
}
