package com.nba.tests.bulls;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"com.nba.tests.bulls.stepdefinitions", "com.nba.framework.hooks"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports",
        "json:target/cucumber-reports/Cucumber.json",
        "junit:target/cucumber-reports/Cucumber.xml"
    },
    monochrome = true,
    publish = true
)
public class BullsTestRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
