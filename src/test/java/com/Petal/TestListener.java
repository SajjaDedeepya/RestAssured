package com.Petal;

import java.util.HashMap;
import java.util.Map;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class TestListener implements ITestListener {
    private ExtentReports extent;
    private ExtentTest test;

    // Map to store custom test names
    private static final Map<String, String> TEST_NAME_MAPPING = new HashMap<>();

    static {
        // Map TestNG method names to custom display names
        TEST_NAME_MAPPING.put("testFindPatient", "Find Patient Test");
        TEST_NAME_MAPPING.put("patientdatareac", "Patient Data Read Test");
        TEST_NAME_MAPPING.put("getServiceTypes", "Get Service Types Test");
        TEST_NAME_MAPPING.put("lockAvailability", "Lock Availability Test");
        TEST_NAME_MAPPING.put("renewAvailability", "Renew Availability Test");
        TEST_NAME_MAPPING.put("bookAppointment", "Book Appointment Test");
        TEST_NAME_MAPPING.put("cancelAppointment", "Cancel Appointment Test");
        TEST_NAME_MAPPING.put("createTask", "Create Task Test");
        TEST_NAME_MAPPING.put("updateQuestionnaire", "Update Questionnaire Test");
        TEST_NAME_MAPPING.put("getServiceRequest", "Get Service Request Test");
        TEST_NAME_MAPPING.put("updateServiceRequest", "Update Service Request Test");
    }

    @Override
    public void onStart(ITestContext context) {
        extent = ExtentReportManager.getInstance();
        System.out.println("TestListener: onStart called, ExtentReports initialized.");
    }

    @Override
    public void onTestStart(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        // Use custom name if available, otherwise fall back to method name
        String displayName = TEST_NAME_MAPPING.getOrDefault(methodName, methodName);
        test = ExtentReportManager.createTest(displayName);
        System.out.println("TestListener: onTestStart called for " + displayName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS, "Test passed");
        System.out.println("TestListener: onTestSuccess called for " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.log(Status.FAIL, "Test failed: " + result.getThrowable());
        System.out.println("TestListener: onTestFailure called for " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.log(Status.SKIP, "Test skipped");
        System.out.println("TestListener: onTestSkipped called for " + result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        try {
            System.out.println("TestListener: onFinish called, generating ExtentReports...");
            extent.flush();
            System.out.println("TestListener: ExtentReports generated successfully.");
        } catch (Exception e) {
            System.err.println("TestListener: Failed to generate ExtentReports: " + e.getMessage());
            e.printStackTrace();
        }
    }
}