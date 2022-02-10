/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hsdag.debugging;

import at.tugraz.ist.ase.cacdr.algorithms.hsdag.Node;
import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.eval.test.TestCase;
import at.tugraz.ist.ase.knowledgebases.core.Constraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * A data structure representing a node of an HS-dag.
 * Supports to identify all diagnosis of a set of test cases.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Getter
@NoArgsConstructor
@Slf4j
public class TestCaseNode extends Node {
    @Setter
    private TestCase testCase;
    @Setter
    private Set<TestCase> TC;

    /**
     * Constructor for the root node.
     */
    public static TestCaseNode createRoot(@NonNull Set<Constraint> label,
                                          @NonNull Set<Constraint> C,
                                          @NonNull TestCase testCase,
                                          @NonNull Set<TestCase> TC) {
        TestCaseNode root = new TestCaseNode();
        root.setLabel(label);
        root.setC(C);
        root.testCase = testCase;
        root.TC = TC;

        log.trace("{}Created root node with [label={}]", LoggerUtils.tab, label);
        return root;
    }

    @Override
    public String toString() {
        return "TestCaseHSDAGNode{" +
                super.toString() +
                ", testCase=" + testCase +
                ", TC=" + TC +
                '}';
    }
}
