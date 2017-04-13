package cn.bit.exec;

import java.util.List;

/**
 * Created by zhehua on 11/04/2017.
 */
public class TestEntity {
    public static final int ENTRY_TYPE_TEST = 2;
    public static final int ENTRY_TYPE_CLASS = 1;

    List<String> testCases;
    String selectedCase;

    String testName;
    String testArg;
    TestStatus testStatus;
    int entryType = ENTRY_TYPE_TEST;

    // test executable
    public TestEntity(List<String> testCases, String testName, String testArg, TestStatus testStatus, String selectedCase) {
        this.testCases = testCases;
        this.testName = testName;
        this.testArg = testArg;
        this.testStatus = testStatus;
        this.selectedCase = selectedCase;
    }

    // test class
    public TestEntity(String className) {
        this.testName = className;
        entryType = ENTRY_TYPE_CLASS;
    }

    public enum TestStatus {
        PASSED,
        FAILED,
        RUNNING
    }

    public String getTestName() {
        return testName;
    }

    public TestStatus getTestStatus() {
        return testStatus;
    }

    public List<String> getTestCases() {
        return testCases;
    }

    public int getEntryType() {
        return entryType;
    }

    @Override
    public String toString() {
        if(testCases.isEmpty())
            return null;
        return selectedCase;
    }

    public String getSelectedCase() {
        return selectedCase;
    }
}
