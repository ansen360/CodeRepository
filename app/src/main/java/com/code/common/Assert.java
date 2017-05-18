package com.code.common;

//import android.os.Debug;
//import android.os.Process;
//import java.lang.Runtime;

/**
 * assertion helper functions.
 * use it when: 
 *    1. define Contract explicitly, for method I/O, or between blocks inside one method.  
 *    2. find bugs that ONLY caused by programmer's carelessness.
 * NOTE: this class is an "easier to use / customize" replacement for java assert syntax mechanism.
 */
public final class Assert {
    private Assert() {}

    /**
     * fail unconditionally. same as fail(false, msg).
     * current implementation is: 
     *    output a fatal log with stack trace, under the log tag ""ASSERTION-FAILED".
     */
    public static void fail(String msg) {
        if (msg == null)
            msg = "invoke trace";
    }

    /**
     * assert condition is true. 
     */
    public static void fail(boolean condition, String msg) {
        if (!condition)
            fail(msg);
    }

    /**
     * assert that condition is TRUE.
     */
    public static void isTrue(boolean condition) {
        fail(condition, null);
    }

    /**
     * assert that condition is TRUE.
     */
    public static void isTrue(boolean condition, String msg) {
        fail(condition, msg);
    }

    /**
     * assert that condition is FALSE.
     */
    public static void isFalse(boolean condition) {
        fail(!condition, null);
    }

    /**
     * assert that condition is FALSE.
     */
    public static void isFalse(boolean condition, String msg) {
        fail(!condition, msg);
    }

    /**
     * assert that object IS null.
     */
    public static void isNull(Object o) {
        fail(o == null, null);
    }

    /**
     * assert that object IS null.
     */
    public static void isNull(Object o, String msg) {
        fail(o == null, msg);
    }

    /**
     * assert that object IS NOT null.
     */
    public static void notNull(Object o) {
        fail(o != null, null);
    }

    /**
     * assert that object IS NOT null.
     */
    public static void notNull(Object o, String msg) {
        fail(o != null, msg);
    }

    /**
     * assert that at least one of two object IS null.
     */
    public static void oneIsNull(Object o1, Object o2) {
        fail(o1 == null || o2 == null, null);
    }

    /**
     * assert that at least one of two object IS null.
     */
    public static void oneIsNull(Object o1, Object o2, String msg) {
        fail(o1 == null || o2 == null, msg);
    }

    /**
     * assert that at least one of two object IS NOT null.
     */
    public static void oneIsNotNull(Object o1, Object o2) {
        fail(o1 != null || o2 != null, null);
    }

    /**
     * assert that at least one of two object IS NOT null.
     */
    public static void oneIsNotNull(Object o1, Object o2, String msg) {
        fail(o1 != null || o2 != null, msg);
    }

    /**
     * assert that string IS NOT Null or Empty (NE), note string still can be white spaces.
     */
    public static void notNE(String value) {
        fail((value != null && value.length() != 0), "value = " + value);
    }

    /**
     * assert that string IS NOT Null or Empty (NE)
     */
    public static void notNE(String value, String msg) {
        fail((value != null && value.length() != 0), msg);
    }

    /**
     * assert that string IS NOT Null or Empty or White Space (NEWS)
     */
    public static void notNEWS(String value) {
        fail((value != null && value.trim().length() != 0), "value = " + value);
    }

    /**
     * assert that string IS NOT Null or Empty or White Space (NEWS),
     */
    public static void notNEWS(String value, String msg) {
        fail((value != null && value.trim().length() != 0), msg);
    }

    /**
     * assert that int must be in range [lowerBoundInclusive, upperBoundExclusive).
     */
    public static void inRange(int value, int lowerBoundInclusive, int upperBoundExclusive) {
        fail(value >= lowerBoundInclusive && value < upperBoundExclusive, String.format("value = %d, range = [%d, %d)", value, lowerBoundInclusive, upperBoundExclusive));
    }

    /**
     * assert that int must be in range [lowerBoundInclusive, upperBoundExclusive).
     */
    public static void inRange(int value, int lowerBoundInclusive, int upperBoundExclusive, String msg) {
        fail(value >= lowerBoundInclusive && value < upperBoundExclusive, msg);
    }

    /**
     * assert that two objects are equal (according java equals semantics).
     */
    public static void equal(Object first, Object second) {
        equal(first, second, String.format("first = %s, second = %s", first, second));
    }

    /**
     * assert that two objects are equal (according java equals semantics).
     */
    public static void equal(Object first, Object second, String msg) {
        // two nulls are equal
        if (first == null && second == null)
            return;
        // null don't equal to non-null
        if (first == null || second == null)
            fail(msg);
        // check if two non-null objects equal
        if (!first.equals(second))
            fail(msg);
    }

    /**
     * assert that two objects are NOT equal (according java equals semantics).
     */
    public static void notEqual(Object first, Object second) {
        notEqual(first, second, String.format("first = %s, second = %s", first, second));
    }

    /**
     * assert that two objects are NOT equal (according java equals semantics).
     */
    public static void notEqual(Object first, Object second, String msg) {
        // two nulls are equal
        if (first == null && second == null)
            fail(msg);
        // null don't equal to non-null
        if (first == null || second == null)
            return;
        // check if two non-null objects equal
        if (first.equals(second))
            fail(msg);
    }
}
