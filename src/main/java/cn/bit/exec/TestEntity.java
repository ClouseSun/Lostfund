package cn.bit.exec;

import oracle.jrockit.jfr.StringConstantPool;

import java.util.List;

/**
 * Created by zhehua on 11/04/2017.
 */
public class TestEntity {
    public static final int ENTRY_TYPE_TEST = 2;
    public static final int ENTRY_TYPE_CLASS = 1;
    List<String> options;
    String testName;
    String testArg;
    TestStatus testStatus;
    int entryType = ENTRY_TYPE_TEST;

    // test executable
    public TestEntity(List<String> options, String testName, String testArg, TestStatus testStatus) {
        this.options = options;
        this.testName = testName;
        this.testArg = testArg;
        this.testStatus = testStatus;
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
}
