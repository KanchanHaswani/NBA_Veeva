package com.nba.framework.assertions;

import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class BaseAssertion {
    
    private SoftAssert softAssert;
    
    public BaseAssertion() {
        this.softAssert = new SoftAssert();
    }
    
    public void assertEquals(String actual, String expected, String message) {
        try {
            Assert.assertEquals(actual, expected, message);
            System.out.println("✓ Assertion passed: " + message);
        } catch (AssertionError e) {
            System.err.println("✗ Assertion failed: " + message + " - Expected: " + expected + ", Actual: " + actual);
            throw e;
        }
    }
    
    public void assertEquals(String actual, String expected) {
        assertEquals(actual, expected, "String comparison");
    }
    
    public void assertEquals(int actual, int expected, String message) {
        try {
            Assert.assertEquals(actual, expected, message);
            System.out.println("✓ Assertion passed: " + message);
        } catch (AssertionError e) {
            System.err.println("✗ Assertion failed: " + message + " - Expected: " + expected + ", Actual: " + actual);
            throw e;
        }
    }
    
    public void assertEquals(int actual, int expected) {
        assertEquals(actual, expected, "Integer comparison");
    }
    
    public void assertTrue(boolean condition, String message) {
        try {
            Assert.assertTrue(condition, message);
            System.out.println("✓ Assertion passed: " + message);
        } catch (AssertionError e) {
            System.err.println("✗ Assertion failed: " + message + " - Expected: true, Actual: " + condition);
            throw e;
        }
    }
    
    public void assertTrue(boolean condition) {
        assertTrue(condition, "Boolean true assertion");
    }
    
    public void assertFalse(boolean condition, String message) {
        try {
            Assert.assertFalse(condition, message);
            System.out.println("✓ Assertion passed: " + message);
        } catch (AssertionError e) {
            System.err.println("✗ Assertion failed: " + message + " - Expected: false, Actual: " + condition);
            throw e;
        }
    }
    
    public void assertFalse(boolean condition) {
        assertFalse(condition, "Boolean false assertion");
    }
    
    public void assertNotNull(Object object, String message) {
        try {
            Assert.assertNotNull(object, message);
            System.out.println("✓ Assertion passed: " + message);
        } catch (AssertionError e) {
            System.err.println("✗ Assertion failed: " + message + " - Object is null");
            throw e;
        }
    }
    
    public void assertNotNull(Object object) {
        assertNotNull(object, "Object not null assertion");
    }
    
    public void assertNull(Object object, String message) {
        try {
            Assert.assertNull(object, message);
            System.out.println("✓ Assertion passed: " + message);
        } catch (AssertionError e) {
            System.err.println("✗ Assertion failed: " + message + " - Object is not null");
            throw e;
        }
    }
    
    public void assertNull(Object object) {
        assertNull(object, "Object null assertion");
    }
    
    public void assertContains(String actual, String expected, String message) {
        try {
            Assert.assertTrue(actual.contains(expected), message);
            System.out.println("✓ Assertion passed: " + message);
        } catch (AssertionError e) {
            System.err.println("✗ Assertion failed: " + message + " - '" + actual + "' does not contain '" + expected + "'");
            throw e;
        }
    }
    
    public void assertContains(String actual, String expected) {
        assertContains(actual, expected, "String contains assertion");
    }
    
    // Soft Assertions
    public void softAssertEquals(String actual, String expected, String message) {
        softAssert.assertEquals(actual, expected, message);
    }
    
    public void softAssertTrue(boolean condition, String message) {
        softAssert.assertTrue(condition, message);
    }
    
    public void softAssertFalse(boolean condition, String message) {
        softAssert.assertFalse(condition, message);
    }
    
    public void softAssertNotNull(Object object, String message) {
        softAssert.assertNotNull(object, message);
    }
    
    public void softAssertContains(String actual, String expected, String message) {
        softAssert.assertTrue(actual.contains(expected), message);
    }
    
    public void assertAll() {
        softAssert.assertAll();
    }
    
    public void softAssertAll() {
        softAssert.assertAll();
    }
}
