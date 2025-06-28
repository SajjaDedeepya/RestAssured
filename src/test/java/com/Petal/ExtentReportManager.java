package com.Petal;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;

public class ExtentReportManager {
    private static ExtentReports extent;
    private static ExtentTest test;

    public static ExtentReports getInstance() {
        if (extent == null) {
            // Ensure the target directory exists
            File reportDir = new File("target");
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }

            ExtentSparkReporter spark = new ExtentSparkReporter("target/AutomationTestReport.html");
            spark.config().setDocumentTitle("REST Assured Test Report");
            spark.config().setReportName("API Test Execution Report");
            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        }
        return extent;
    }

    public static ExtentTest createTest(String testName) {
        test = extent.createTest(testName);
        return test;
    }
}