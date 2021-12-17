/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.checker;

import at.tugraz.ist.ase.eval.test.TestCase;

import java.util.Set;

public interface IDebuggingModel {
    /**
     * Gets the set of test cases.
     * @return the set of test cases.
     */
    Set<String> getTestcases();

    /**
     * Gets a corresponding {@link TestCase} object of a textual testcase.
     * @param testcase a textual testcase.
     * @return a corresponding {@link TestCase} object.
     */
    TestCase getTestCase(String testcase);
}
