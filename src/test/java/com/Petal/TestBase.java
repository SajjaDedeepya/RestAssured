package com.Petal;

import org.testng.annotations.BeforeClass;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class TestBase {
    protected RequestSpecification petalSpec;
    protected RequestSpecification vitraiSpec;

    @BeforeClass
    public void setUp() {
        String petalBaseUrl = ConfigLoader.getPetalsASEURL();
        String vitraiBaseUrl = ConfigLoader.getVitraiBASEURL();

        petalSpec = new RequestSpecBuilder()
                .setBaseUri(petalBaseUrl)
                .build();

        vitraiSpec = new RequestSpecBuilder()
                .setBaseUri(vitraiBaseUrl)
                .build();
    }
}